package online.merkatos.prexocertoonline.model_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView

class CustomImageView : ImageView {
    private var canvas: Canvas? = null
    private lateinit var paint: Paint
    private var vheign: Int = 0
    private var vwidth: Int = 0

    constructor(c: Context) : super(c) {
        initButtonView()
    }

    constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
        initButtonView()
    }

    constructor(c: Context, attrs: AttributeSet?, defStyle: Int) : super(c, attrs, defStyle) {
        initButtonView()
    }

    fun initButtonView() {

        paint = Paint()
        paint.flags = Paint.ANTI_ALIAS_FLAG
        paint.color = Color.LTGRAY

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        vwidth = MeasureSpec.getSize(widthMeasureSpec)
        vheign = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(vwidth, vheign)
    }

    override fun onDraw(canvas: Canvas?) {


        val count = canvas!!.saveCount
        canvas.save()
        //draw the a circle and a cross signal in the canvas
        //Define the view center vxcenter, vycenter
        if (this.drawable == null) {

            vwidth = this.width
            vheign = this.height

            val vxc = vwidth.div(2).toFloat()
            val vyc = vheign.div(2).toFloat()

            //Define the radius of the circle
            //Take the minimum size
            val min: Int = Math.min(vwidth, vheign) / 10

            println("MINIMUM: " + min)
            println("MINIMUM VW: " + this.width)
            println("MINIMUM VH: " + this.height)
            println("MINIMUM VT: " + this.top)
            println("MINIMUM VL: " + this.left)
            println("MINIMUM VR: " + this.right)
            println("MINIMUM VB: " + this.bottom)
            println("MINIMUM VxC: " + vxc)
            println("MINIMUM VyC: " + vyc)

            //Draw Lines to form a rectangle from the vertices
            val left = vxc - vwidth / 2
            val top = vyc - vheign / 2
            val right = vxc + vwidth / 2
            val bottom = vyc + vheign / 2

            //val rect = Rect(left.toInt(), top.toInt(), right.toInt(),bottom.toInt())

            paint.strokeWidth = 4f


            canvas.drawLines(rectEdges(left, top, right, bottom), paint)

            paint.color = Color.DKGRAY
            paint.textSize = 40f
            //canvas.drawLine(vxc - 45, vyc, vxc + 45, vyc, paint)
            //canvas.drawLine(vxc, vyc - 45, vxc, vyc + 45, paint)

            var hint = "Click to"
            canvas.drawText(hint, (vxc - 64), vyc - 20, paint)
            hint = "add an image"
            canvas.drawText(hint, (vxc - 128), vyc + 30, paint)
            ////canvas.drawText("Click to add", left + 20, vyc, paint)

        } else {
            super.onDraw(canvas)
        }


    }

    fun rectEdges(left: Float, top: Float, right: Float, bottom: Float): FloatArray {
        val pxy = floatArrayOf(left, top)
        val px1y1 = floatArrayOf(left, bottom)
        val px2y2 = floatArrayOf(right, bottom)
        val px3y3 = floatArrayOf(right, top)

        return floatArrayOf(pxy[0], pxy[1], px1y1[0], px1y1[1], px1y1[0], px1y1[1], px2y2[0], px2y2[1], px2y2[0], px2y2[1], px3y3[0], px3y3[1], px3y3[0], px3y3[1], pxy[0], pxy[1])
    }
}