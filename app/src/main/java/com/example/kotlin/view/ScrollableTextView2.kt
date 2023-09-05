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
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.OverScroller
import androidx.core.view.ViewCompat

class ScrollableTextView2 @JvmOverloads constructor(
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
    private val scroller: OverScroller = OverScroller(context)
    private var velocityTracker: VelocityTracker? = null

    init {
        isSaveEnabled = true
    }

    fun setTextList(textList: List<String>?) {
        this.textList = textList
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.v("MYTAG","onDraw")
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
                Log.v("MYTAG","onTouchEvent ACTION_DOWN")
                scrolling = true
                touchY = event.y
                scroller.abortAnimation()
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain()
                } else {
                    velocityTracker?.clear()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                Log.v("MYTAG","onTouchEvent ACTION_MOVE")
                if (scrolling) {
                    val deltaY = touchY - event.y // 修复方向
                    touchY = event.y
                    scrollY1 += deltaY.toInt()

                    // 防止滚动超出顶部或底部边界
                    if (scrollY1 < 0) {
                        scrollY1 = 0
                    } else if (scrollY1 > maxScrollY()) {
                        scrollY1 = maxScrollY()
                    }
                    Log.v("MYTAG","onTouchEvent ACTION_MOVE scrollY1:${scrollY1}")
                    invalidate()
                    velocityTracker?.addMovement(event)
                }
            }
            MotionEvent.ACTION_UP -> {
                Log.v("MYTAG","onTouchEvent ACTION_UP")
                scrolling = false

                // 使用Scroller来实现弹性滑动
                val finalY = ensureScrollBounds()

                // 获取垂直方向的速度
                velocityTracker?.computeCurrentVelocity(1000)
                val velocityY = velocityTracker?.yVelocity?.toInt() ?: 0
                Log.v("MYTAG","velocityY ${velocityY}")
                if (Math.abs(velocityY) >= 1000) { // 根据需要调整速度阈值
                    // 速度过快，直接滑到最顶部或最底部
                    scrollY1 = if (velocityY > 0) 0 else maxScrollY()
                    Log.v("MYTAG","onTouch ACTION_UP speed fast scrollY1:${scrollY1}")
                } else {
                    // 使用Scroller来实现弹性滑动
                    val delayY = finalY - scrollY1
                    scroller.startScroll(0, scrollY1, 0, delayY, 500) // 调整滑动时间和曲线
                    Log.v("MYTAG","onTouch ACTION_UP speed slow scrollY1:${scrollY1},delayY${delayY}")
                    ViewCompat.postInvalidateOnAnimation(this)
                }

                //velocityTracker?.recycle()
            }
        }
        return true
    }

    override fun computeScroll() {
        Log.v("MYTAG","computeScroll")
        if (scroller.computeScrollOffset()) {
            scrollY1 = scroller.currY
            invalidate()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = Bundle()
        savedState.putParcelable("superState", super.onSaveInstanceState())
        savedState.putInt("scrollY", scrollY1)
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            scrollY1 = newState.getInt("scrollY")
            newState = newState.getParcelable("superState")
        }
        super.onRestoreInstanceState(newState)
    }

    private fun maxScrollY(): Int {
        //Log.v("MYTAG","maxScrollY")
        return (textList?.size?.toFloat()?.times(textHeight) ?: 0f).toInt() - height
    }

    private fun ensureScrollBounds(): Int {
        var finalY = scrollY1
        if (scrollY1 < 0) {
            finalY = 0
        } else if (scrollY1 > maxScrollY()) {
            finalY = maxScrollY()
        }
        return finalY
    }
}
