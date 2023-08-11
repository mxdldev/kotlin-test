package com.example.kotlin.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * Description: <IncrementalTextView><br>
 * Author:      mxdl<br>
 * Date:        2023/8/11<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
class IncrementalTextView(context: Context, attrs: AttributeSet?) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {

    // 增量
    private var increment = 0f

    override fun draw(canvas: Canvas?) {
        canvas?.clipRect(0f, 0f, increment, height.toFloat())
        super.draw(canvas)
    }

    fun setIncrement(increment: Float) {
        this.increment = increment
        invalidate()
    }
}