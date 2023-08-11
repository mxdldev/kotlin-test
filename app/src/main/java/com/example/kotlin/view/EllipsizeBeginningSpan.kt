package com.example.kotlin.view

/**
 * Description: <EllipsizeBeginningSpan><br>
 * Author:      mxdl<br>
 * Date:        2023/8/11<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class EllipsizeBeginningSpan(private val widthRatio: Float) : MetricAffectingSpan() {

    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.textScaleX = widthRatio
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.textScaleX = widthRatio
    }
}
