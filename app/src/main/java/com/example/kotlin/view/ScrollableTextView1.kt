package com.example.kotlin.view

/**
 * Description: <ScrollableTextView><br>
 * Author:      mxdl<br>
 * Date:        2023/9/4<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class ScrollableTextView1 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textList: List<String>? = null
    private val textPaint: Paint = Paint().apply {
        isAntiAlias = true
        textSize = 60f
        color = 0xFF000000.toInt() // 文本颜色
        textAlign = Paint.Align.LEFT
    }
    private val textHeight: Float = textPaint.fontSpacing
    private var scrollY1: Int = 0
    private var touchY: Float = 0f
    private var scrolling: Boolean = false

    fun setTextList(textList: List<String>?) {
        this.textList = textList
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        textList?.let {
            var startY = 0

            for (i in textList!!.indices) {
                val lineText = textList!![i]
                canvas.drawText(lineText, 0f, (-scrollY1 + startY + textHeight), textPaint)
                startY += textHeight.toInt()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scrolling = true
                touchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if (scrolling) {
                    val deltaY = touchY - event.y // 反转方向
                    touchY = event.y
                    scrollY1 += deltaY.toInt()

                    // 防止滚动超出顶部或底部边界
                    if (scrollY1 < 0) {
                        scrollY1 = 0
                    } else if (scrollY1 > maxScrollY()) {
                        scrollY1 = maxScrollY()
                    }

                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> scrolling = false
        }
        return true
    }

    private fun maxScrollY(): Int {
        return (textList?.size?.toFloat()?.times(textHeight) ?: 0f).toInt() - height
    }
}
