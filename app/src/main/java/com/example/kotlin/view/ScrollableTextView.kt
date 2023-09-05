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
import com.example.kotlin.util.PoetryUntil

class ScrollableTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var textList: List<String>? = null
    private val textPaint: Paint = Paint().apply {
        isAntiAlias = true
        textSize = 60f
        color = 0xFF000000.toInt() // 文本颜色
        textAlign = Paint.Align.LEFT
    }
    private val textHeight: Float = textPaint.fontSpacing
    private var mCurrSscrollY: Int = 0
    private var touchY: Float = 0f
    private var scrolling: Boolean = false
    private val scroller = OverScroller(context)
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
        Log.v("MYTAG", "onDraw scrollY1:${mCurrSscrollY}")
        if(mCurrSscrollY < 0){
            mCurrSscrollY = 0
        }else if(mCurrSscrollY > maxScrollY()){
            mCurrSscrollY = maxScrollY()
        }
        textList?.let {
            var startY = 0

            for (i in textList!!.indices) {
                val lineText = textList!![i]
                canvas.drawText(lineText, 0f, (-mCurrSscrollY + startY + textHeight), textPaint)
                startY += textHeight.toInt()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.v("MYTAG", "onTouchEvent ACTION_MOVE scrollY1:${mCurrSscrollY}")
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
                if (scrolling) {
                    val deltaY = touchY - event.y // 计算滑动距离
                    touchY = event.y
                    mCurrSscrollY += deltaY.toInt()

                    // 防止滚动超出顶部或底部边界
                    if (mCurrSscrollY < 0) {
                        mCurrSscrollY = 0
                    } else if (mCurrSscrollY > maxScrollY()) {
                        mCurrSscrollY = maxScrollY()
                    }
                    Log.v("MYTAG", "onTouchEvent ACTION_MOVE scrollY1:${mCurrSscrollY}")
                    invalidate()
                    velocityTracker?.addMovement(event)
                }
            }
            MotionEvent.ACTION_UP -> {
                scrolling = false

                // 使用Scroller来实现弹性滑动
                val finalY = ensureScrollBounds()

                // 获取垂直方向的速度
                velocityTracker?.computeCurrentVelocity(1000)
                val velocityY = velocityTracker?.yVelocity ?: 0f

                // 速度大于等于1000，处理弹性滑动逻辑
                if (Math.abs(velocityY) >= 1000) {
                    val maxVelocityY = 20000f // 设置最大速度

                    // 根据速度大小调整滚动距离
                    val scrollDistance = (velocityY / maxVelocityY) * maxScrollY()

                    val duration = 500L // 弹性滑动的持续时间

                    // 计算距离顶部和底部的距离
                    val distanceToTop = mCurrSscrollY
                    val distanceToBottom = maxScrollY() - mCurrSscrollY

                    // 如果速度足够快且距离边界不足100像素，滚动到顶部或底部
                    if (Math.abs(velocityY) >= 1000 && (distanceToTop < PoetryUntil.dpToPx(context,80f) || distanceToBottom < PoetryUntil.dpToPx(context,80f))) {
                        if (velocityY > 0) {
                            scroller.startScroll(0, mCurrSscrollY, 0, -distanceToTop, duration.toInt())
                        } else {
                            scroller.startScroll(0, mCurrSscrollY, 0, distanceToBottom, duration.toInt())
                        }
                        Log.v("MYTAG", "onTouchEvent ACTION_UP speed fast <80 scrollY1:${mCurrSscrollY}")
                    } else {
                        // 否则，根据速度大小调整滚动距离
                        var adjustedScrollDistance = scrollDistance

                        // 修正滚动距离，确保不超出边界
                        if (mCurrSscrollY + scrollDistance < 0) {
                            adjustedScrollDistance = -mCurrSscrollY.toFloat()
                        } else if (mCurrSscrollY + scrollDistance > maxScrollY()) {
                            adjustedScrollDistance = (maxScrollY() - mCurrSscrollY).toFloat()
                        }
                        Log.v("MYTAG", "onTouchEvent ACTION_UP speed fast >80 scrollY1:${mCurrSscrollY},adjustedScrollDistance:${adjustedScrollDistance}")
                        // 使用Scroller来实现弹性滑动
                        scroller.startScroll(0, mCurrSscrollY, 0, -adjustedScrollDistance.toInt(), duration.toInt())
                    }

                    ViewCompat.postInvalidateOnAnimation(this)
                } else {
                    // 速度小于1000，根据需要进行其他逻辑处理
                    // 在这里添加你的其他逻辑
                    val delayY = finalY - mCurrSscrollY
                    scroller.startScroll(0, mCurrSscrollY, 0, delayY, 500) // 调整滑动时间和曲线
                    Log.v("MYTAG","onTouch ACTION_UP speed slow scrollY1:${mCurrSscrollY},delayY${delayY}")
                    ViewCompat.postInvalidateOnAnimation(this)
                }

                velocityTracker = null
            }
        }
        return true
    }




    override fun computeScroll() {
        Log.v("MYTAG", "computeScroll")
        if (scroller.computeScrollOffset()) {
            mCurrSscrollY = scroller.currY
            invalidate()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = Bundle()
        savedState.putParcelable("superState", super.onSaveInstanceState())
        savedState.putInt("scrollY", mCurrSscrollY)
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var newState = state
        if (newState is Bundle) {
            mCurrSscrollY = newState.getInt("scrollY")
            newState = newState.getParcelable("superState")
        }
        super.onRestoreInstanceState(newState)
    }

    private fun maxScrollY(): Int {
        val textHeight = (textList?.size?.toFloat()?.times(textHeight) ?: 0f).toInt()
        Log.v("MYTAG","maxScrollY textHeight:${textHeight},viewHeight:${height}")
        return textHeight - height
    }

    private fun ensureScrollBounds(): Int {
        var finalY = mCurrSscrollY
        if (mCurrSscrollY < 0) {
            finalY = 0
        } else if (mCurrSscrollY > maxScrollY()) {
            finalY = maxScrollY()
        }
        return finalY
    }
}
