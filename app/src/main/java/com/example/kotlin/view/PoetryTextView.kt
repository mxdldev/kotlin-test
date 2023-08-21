package com.example.kotlin.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.example.kotlin.entity.Poetry
import com.example.kotlin.util.PoetryUntil

/**
 * Description: <PoetryTextView><br>
 * Author:      mxdl<br>
 * Date:        2023/8/21<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
@SuppressLint("AppCompatCustomView") class PoetryTextView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {
    val titlePaint = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 28f)
        color = Color.BLACK
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
        isFakeBoldText = true
    }
    val authorPaint = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 16f)
        color = Color.parseColor("#666666")
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
    }
    val contentPaint = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 24f)
        color = Color.parseColor("#212121")
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
    }

    val poetryMarginTop = PoetryUntil.dpToPx(context, 40f)
    var poetry: Poetry? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        poetry?.poem?.let {
            var currHeight = poetryMarginTop;
            val screenWidth = PoetryUntil.getScreenWidth(context)
            it.title?.let {
                val titleX = screenWidth / 2 - PoetryUntil.getTextWidth(titlePaint, it.name) / 2
                val titleY = currHeight
                canvas.drawText(it.name, titleX, titleY, titlePaint)
                currHeight += PoetryUntil.getTextHeight(titlePaint)
            }
            it.poet?.let {
                val authorX = screenWidth / 2 - PoetryUntil.getTextWidth(authorPaint, it.name) / 2
                val authorY = currHeight
                canvas.drawText(it.name, authorX, authorY, authorPaint)
                currHeight += PoetryUntil.getTextHeight(authorPaint)
            }
            currHeight += PoetryUntil.dpToPx(context, 20f)
            it.content?.let {
                it.forEach {
                    val contentX = screenWidth / 2 - PoetryUntil.getTextWidth(contentPaint, it.label) / 2 + PoetryUntil.dpToPx(context, 15f)
                    val contentY = currHeight
                    canvas.drawText(it.label, contentX, contentY, contentPaint)
                    currHeight = currHeight + PoetryUntil.getTextHeight(contentPaint) + PoetryUntil.dpToPx(context, 15f)
                }
            }
        }
    }

    fun initData(poetry: Poetry?) {
        this.poetry = poetry
        invalidate()
    }
}