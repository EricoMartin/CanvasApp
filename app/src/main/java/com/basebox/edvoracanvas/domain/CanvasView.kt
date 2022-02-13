package com.basebox.edvoracanvas.domain

import android.R.attr
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.basebox.edvoracanvas.MainActivity.Companion.paintColor
import com.basebox.edvoracanvas.MainActivity.Companion.path
import com.basebox.edvoracanvas.R


private const val STROKE_WIDTH = 12f

class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        init()
    }

    //Variables for caching
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private val drawing = Path()

    private val curPath = Path()

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var frame: Rect

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.white, null)

    private fun init() {
        paintColor.apply {
            color = brushColor
            // Smooths out edges of what is drawn without affecting shape.
            isAntiAlias = true
            // Dithering affects how colors with higher-precision than the device are down-sampled.
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = STROKE_WIDTH
        }
    }

    private var paint = paintColor

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in pathList.indices) {
            paint.color = colorList[i]
            canvas.drawPath(pathList[i], paint)

        }

        canvas.drawPath(path, paint)

        for (i in shapeType.indices) {
            if (shapeType[i] == "rectangle") {
                val RADIUS = 100
                val rect = Rect(
                    ((motionTouchEventX - ((0.8) * RADIUS)).toInt()),
                    ((motionTouchEventY - ((0.6) * RADIUS)).toInt()),
                    ((motionTouchEventX + ((0.8) * RADIUS)).toInt()),
                    ((motionTouchEventY + ((0.6 * RADIUS))).toInt())
                )
                canvas.drawRect(rect, paint)
            } else if (shapeType[i] == "oval") {

                canvas.drawCircle(
                    motionTouchEventX / 2.0f,
                    motionTouchEventY / 2.0f,
                    (width - 10) / 2.0f,
                    paint
                )
            } else if (shapeType[i] == "arrow") {
                val radius = 100 //Radius of blue circle to the right

                val leftArrow = Path()
                val leftArrowPaint = Paint(Paint.ANTI_ALIAS_FLAG)

                leftArrowPaint.style = Paint.Style.STROKE
//                leftArrowPaint.color = colorList[i]
                leftArrowPaint.alpha = 80
                leftArrowPaint.strokeWidth = 8F
                leftArrowPaint.strokeJoin = Paint.Join.BEVEL
                leftArrow.moveTo(
                    motionTouchEventX - (attr.radius + 5),
                    (motionTouchEventY).toFloat()
                )

                leftArrow.lineTo(
                    motionTouchEventX - (attr.radius + 60),
                    (motionTouchEventY).toFloat()
                )

                leftArrow.lineTo(
                    motionTouchEventX - (attr.radius + 30),
                    (motionTouchEventY - 30).toFloat()
                )

                leftArrow.moveTo(
                    motionTouchEventX - (attr.radius + 60),
                    (motionTouchEventY).toFloat()
                )

                leftArrow.lineTo(
                    motionTouchEventX - (attr.radius + 30),
                    (motionTouchEventY + 30).toFloat()
                )
                canvas.drawPath(leftArrow, leftArrowPaint)
                leftArrow.reset()
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to cache it.
            extraCanvas.drawPath(path, paint)
            colorList.add(brushColor)
            pathList.add(path)
        }
        invalidate()
    }

    private fun touchUp() {
        drawing.addPath(curPath)
        curPath.reset()
        path.reset()
    }

    companion object {
        var shapeType = ArrayList<String>()
        var pathList = ArrayList<Path>()
        var colorList = ArrayList<Int>()
        var brushColor = Color.BLACK


    }
}