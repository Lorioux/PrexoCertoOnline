package online.merkatos.prexocertoonline.models

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import online.merkatos.prexocertoonline.R

/**
 * Example of writing a custom layout manager.  This is a fairly full-featured
 * layout manager that is relatively general, handling all layout cases.  You
 * can simplify it for more specific cases.
 */
@RemoteViews.RemoteView
open class CustomLayout_ : ViewGroup {


    /** The amount of space used by children in the left gutter. */
    private var mLeftWidth = 0

    /** The amount of space used by children in the right gutter. */
    private var mRightWidth: Int
        get(): Int {
            return mRightWidth
        }
        set(rw: Int) {
            mRightWidth = rw
        }

    /** These are used for computing child frames based on their gravity. */
    private val mTmpContainerRect: Rect = Rect()
    private val mTmpChildRect: Rect = Rect()

    //constructor( context: Context): super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, 0)

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = this.childCount

        // These keep track of the space we are using on the left and right for
        // views positioned there; we need member variables so we can also use
        // these for layout later.
        mLeftWidth = 0
        mRightWidth = 0

        // Measurement will ultimately be computing these values.
        var maxHeight = 0
        var maxWidth = 0
        var childState = 0

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
        for (i: Int in 0 until count) {

            val child = getChildAt(i)

            if (child.visibility != GONE) {
                // Measure the child.
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)

                // Update our size information based on the layout params.  Children
                // that asked to be positioned on the left or right go in those gutters.
                val lp = child.layoutParams as CustomLayoutParams
                if (lp.position == CustomLayoutParams.POSITION_LEFT) {
                    mLeftWidth += Math.max(maxWidth,
                            child.measuredWidth + lp.leftMargin + lp.rightMargin)
                } else if (lp.position == CustomLayoutParams.POSITION_RIGHT) {
                    mRightWidth += Math.max(maxWidth,
                            child.measuredWidth + lp.leftMargin + lp.rightMargin)
                } else {
                    maxWidth = Math.max(maxWidth,
                            child.measuredWidth + lp.leftMargin + lp.rightMargin)
                }
                maxHeight = Math.max(maxHeight,
                        child.measuredHeight + lp.topMargin + lp.bottomMargin)
                childState = combineMeasuredStates(childState, child.measuredState)
            }
        }

        // Total width is the maximum width of all inner children plus the gutters.
        maxWidth += mLeftWidth + mRightWidth

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)
        maxWidth = Math.max(maxWidth, suggestedMinimumWidth)

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState shl MEASURED_HEIGHT_STATE_SHIFT))
    }

    /**
     * Position all children within this layout.
     */
    //@Override
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val count: Int = childCount

        // These are the far left and right edges in which we are performing layout.
        var leftPos = paddingLeft
        var rightPos = right - left - this.paddingRight

        // This is the middle region inside of the gutter.
        val middleLeft = leftPos + mLeftWidth
        val middleRight: Int = rightPos - mRightWidth

        // These are the top and bottom edges in which we are performing layout.
        val parentTop: Int = paddingTop
        val parentBottom: Int = bottom - top - this.paddingBottom

        for (i in 0..(count - 1)) {
            val child: View = getChildAt(i)

            if (child.visibility != GONE) {

                val lp: CustomLayoutParams = child.layoutParams as CustomLayoutParams

                val width = child.measuredWidth
                val height = child.measuredHeight

                // Compute the frame in which we are placing this child.
                if (lp.position == CustomLayoutParams.POSITION_MIDDLE) {
                    mTmpContainerRect.left = leftPos + lp.leftMargin
                    mTmpContainerRect.right = leftPos + width + lp.rightMargin
                    leftPos = mTmpContainerRect.right

                } else if (lp.position == CustomLayoutParams.POSITION_RIGHT) {

                    mTmpContainerRect.right = rightPos - lp.rightMargin
                    mTmpContainerRect.left = rightPos - width - lp.leftMargin
                    rightPos = mTmpContainerRect.left

                } else {
                    mTmpContainerRect.left = middleLeft + lp.leftMargin
                    mTmpContainerRect.right = middleRight - lp.rightMargin
                }
                mTmpContainerRect.top = parentTop + lp.topMargin
                mTmpContainerRect.bottom = parentBottom - lp.bottomMargin

                // Use the child's gravity and size to determine its final
                // frame within its container.
                Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect)

                // Place the child.
                child.layout(mTmpChildRect.left, mTmpChildRect.top,
                        mTmpChildRect.right, mTmpChildRect.bottom)
            }
        }
    }

    // ----------------------------------------------------------------------
    // The rest of the implementation is for custom per-child layout parameters.
    // If you do not need these (for example you are writing a layout manager
    // that does fixed positioning of its children), you can drop all of this.

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return CustomLayout.LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): CustomLayoutParams {
        return CustomLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): CustomLayoutParams {
        return CustomLayoutParams(p)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is CustomLayoutParams
    }

    /**
     * Custom per-child layout information.
     */
    class CustomLayoutParams : MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
        var gravity = Gravity.TOP or Gravity.START

        var position = POSITION_MIDDLE


        constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
            //super(c, attrs);
            // Pull the layout param values from the layout XML during
            // inflation.  This is not needed if you don't care about
            // changing the layout behavior in XML.
            val a: TypedArray = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP)
            gravity = a.getInt(R.styleable.CustomLayoutLP_android_layout_gravity, gravity)
            position = a.getInt(R.styleable.CustomLayoutLP_layout_position, position)
            a.recycle()
        }

        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: ViewGroup.LayoutParams) : super(source)

        companion object {
            var POSITION_MIDDLE: Int = 0
            var POSITION_LEFT: Int = 1
            var POSITION_RIGHT: Int = 2
        }
    }
}

/*
If you are implementing XML layout attributes as shown in the example, this is the corresponding definition for them that would go in res/values/attrs.xml:
<declare-styleable name="CustomLayoutLP">
<attr name="android:layout_gravity" />
<attr name="layout_position">
<enum name="middle" value="0" />
<enum name="left" value="1" />
<enum name="right" value="2" />
</attr>
</declare-styleable>
Finally the layout manager can be used in an XML layout like so:
<com.example.android.apis.view.CustomLayout
xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res/com.example.android.apis"
android:layout_width="match_parent"
android:layout_height="match_parent">

<!-- put first view to left. -->
<TextView
android:background="@drawable/filled_box"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
app:layout_position="left"
android:layout_gravity="fill_vertical|center_horizontal"
android:text="l1"/>

<!-- stack second view to left. -->
<TextView
android:background="@drawable/filled_box"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
app:layout_position="left"
android:layout_gravity="fill_vertical|center_horizontal"
android:text="l2"/>

<!-- also put a view on the right. -->
<TextView
android:background="@drawable/filled_box"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
app:layout_position="right"
android:layout_gravity="fill_vertical|center_horizontal"
android:text="r1"/>

<!-- by default views go in the middle; use fill vertical gravity -->
<TextView
android:background="@drawable/green"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_gravity="fill_vertical|center_horizontal"
android:text="fill-vert"/>

<!-- by default views go in the middle; use fill horizontal gravity -->
<TextView
android:background="@drawable/green"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_gravity="center_vertical|fill_horizontal"
android:text="fill-horiz"/>

<!-- by default views go in the middle; use top-left gravity -->
<TextView
android:background="@drawable/blue"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_gravity="top|left"
android:text="top-left"/>

<!-- by default views go in the middle; use center gravity -->
<TextView
android:background="@drawable/blue"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_gravity="center"
android:text="center"/>

<!-- by default views go in the middle; use bottom-right -->
<TextView
android:background="@drawable/blue"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:layout_gravity="bottom|right"
android:text="bottom-right"/>

</com.example.android.apis.view.CustomLayout>*/
