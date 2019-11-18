package online.merkatos.prexocertoonline.models

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import online.merkatos.prexocertoonline.R

class ProductImagePlaceHolder : AppCompatImageView {
    private var mPaint: Paint? = null
    private var mRectangle: Rect? = null
    private var mwidth = 0
    private var mheight = 0
    private var rx = 0f
    private var ry = 0f
    private var cx = 0
    private var cy = 0


    private fun init(attrs: AttributeSet?) {
        //val typedArrey: TypedArray? = null
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = Color.LTGRAY
        mPaint!!.textSize = 40f

        mRectangle = Rect()
        cx = mRectangle!!.centerX()
        cy = mRectangle!!.centerY()

    }

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)

        var ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP)
        //mwidth = ta.getInt(R.styleable.CustomLayoutLP_android_layout_gravity,1)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mwidth = MeasureSpec.getSize(widthMeasureSpec)
        maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        //Retrieve this view position
        this.rx = this.x
        this.ry = this.x

        setMeasuredDimension(width, mwidth)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.rx = this.x
        this.ry = this.x
        mwidth = w
        mheight = h
    }

    override fun onDraw(canvas: Canvas?) {
        //super.onDraw(canvas)

        //canvas.drawRe
    }

    var canvas = Canvas()

}