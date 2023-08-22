package com.example.kotlin.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.example.kotlin.entity.Poetry
import com.example.kotlin.entity.PoetryPlayType
import com.example.kotlin.util.PoetryUntil

/**
 * Description: <PoetryTextView><br>
 * Author:      mxdl<br>
 * Date:        2023/8/21<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
@SuppressLint("AppCompatCustomView") class PoetryTextView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {
    val mTitlePaint = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 28f)
        color = Color.BLACK
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
        isFakeBoldText = true
    }
    val mTitlePaintRed = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 28f)
        color = Color.RED
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
        isFakeBoldText = true
    }
    val mAuthorPaint = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 16f)
        color = Color.parseColor("#666666")
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
    }
    val mContentPaint = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 24f)
        color = Color.parseColor("#212121")
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
    }

    val mPoetryMarginTop = PoetryUntil.dpToPx(context, 40f)
    var mPoetry: Poetry? = null
    var mTotalTime = 0L
    var mProcess = 0L
    var mPlayType = PoetryPlayType.TITLE
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        mPoetry?.poem?.let {
            var currHeight = mPoetryMarginTop;
            val screenWidth = PoetryUntil.getScreenWidth(context)
            it.title?.let {
                val titleWidth = PoetryUntil.getTextWidth(mTitlePaint, it.name)
                val titleHeight = PoetryUntil.getRealTextHeight(mTitlePaint)
                val titleX = screenWidth / 2 - titleWidth / 2
                val titleY = currHeight
                canvas.drawText(it.name, titleX, titleY, mTitlePaint)
                currHeight += PoetryUntil.getTextHeight(mTitlePaint)

                if (mPlayType == PoetryPlayType.TITLE && mTotalTime > 0) {
                    var currTitleWidth = titleWidth * (mProcess / mTotalTime.toFloat())
                    if (currTitleWidth > titleWidth) {
                        currTitleWidth = titleWidth
                    }
                    //Log.v("MYTAG","titleWidth:${titleWidth},currTitleWidth:${currTitleWidth}")
                    val left = titleX
                    val top = titleY - titleHeight
                    val right = titleX + currTitleWidth
                    val bottom = titleY + titleHeight
                    Log.v("MYTAG", "left:${left},top:${top},right:${right},bottom:${bottom}")
                    canvas.clipRect(RectF(left, top, right, bottom))
                    canvas.drawText(it.name, titleX, titleY, mTitlePaintRed)
                }
            }
            it.poet?.let {
                val authorX = screenWidth / 2 - PoetryUntil.getTextWidth(mAuthorPaint, it.name) / 2
                val authorY = currHeight
                canvas.drawText(it.name, authorX, authorY, mAuthorPaint)
                currHeight += PoetryUntil.getTextHeight(mAuthorPaint)
            }
            currHeight += PoetryUntil.dpToPx(context, 20f)
            it.content?.let {
                it.forEach {
                    val contentX = screenWidth / 2 - PoetryUntil.getTextWidth(mContentPaint, it.label) / 2 + PoetryUntil.dpToPx(context, 15f)
                    val contentY = currHeight
                    canvas.drawText(it.label, contentX, contentY, mContentPaint)
                    currHeight = currHeight + PoetryUntil.getTextHeight(mContentPaint) + PoetryUntil.dpToPx(context, 15f)
                }
            }
        }
        canvas.restore()
    }

    fun initData(poetry: Poetry?) {
        this.mPoetry = poetry
        invalidate()
    }

    fun setCurrProcess(playType: PoetryPlayType, totalTime: Long, process: Long) {
        Log.v("MYTAG", "totalTime:${totalTime},process:${process},${process / totalTime.toFloat()}")
        mPlayType = playType
        mTotalTime = totalTime
        mProcess = process
        invalidate()
    }
}