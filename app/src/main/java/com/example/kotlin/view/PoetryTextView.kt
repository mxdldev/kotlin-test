package com.example.kotlin.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.example.kotlin.entity.Poetry
import com.example.kotlin.entity.PoetryLine
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
    val mAuthorPaintRed = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 16f)
        color = Color.RED
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
    }
    val mContentPaint = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 24f)
        color = Color.parseColor("#212121")
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
    }
    val mContentPaintRed = Paint().apply {
        textSize = PoetryUntil.spToPx(context, 24f)
        color = Color.RED
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZKTJW.TTF")
    }

    ///////////////////////////////歌词绘画播放器//////////////////////////////////
    val lock = ByteArray(0)
    val mPoetryMarginTop = PoetryUntil.dpToPx(context, 40f)
    var mPoetry: Poetry? = null
    var mPoetryLine: PoetryLine? = null
    override fun onDraw(canvas: Canvas) {
        synchronized(lock) {
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

                    if (mPoetryLine?.type == PoetryPlayType.TITLE.type && mPoetryLine?.process!! > 0 && mPoetryLine?.totalTime!! > 0) {
                        var currTitleWidth = titleWidth * (mPoetryLine?.process!! / mPoetryLine?.totalTime!!.toFloat())
                        if (currTitleWidth > titleWidth) {
                            currTitleWidth = titleWidth
                        }
                        //Log.v("MYTAG","titleWidth:${titleWidth},currTitleWidth:${currTitleWidth}")
                        val left = titleX
                        val top = titleY - titleHeight
                        val right = titleX + currTitleWidth
                        val bottom = titleY + titleHeight
                        //Log.v("MYTAG", "left:${left},top:${top},right:${right},bottom:${bottom}")
                        canvas.save()
                        canvas.clipRect(RectF(left, top, right, bottom))
                        canvas.drawText(it.name, titleX, titleY, mTitlePaintRed)
                        canvas.restore()
                    }
                }
                it.poet?.let {
                    val authorWidth = PoetryUntil.getTextWidth(mAuthorPaint, it.name)
                    val authorHeight = PoetryUntil.getRealTextHeight(mAuthorPaint)

                    val authorX = screenWidth / 2 - PoetryUntil.getTextWidth(mAuthorPaint, it.name) / 2
                    val authorY = currHeight
                    canvas.drawText(it.name, authorX, authorY, mAuthorPaint)
                    currHeight += PoetryUntil.getTextHeight(mAuthorPaint)

                    if (mPoetryLine?.type == PoetryPlayType.AUTHOR.type && mPoetryLine?.process!! > 0 && mPoetryLine?.totalTime!! > 0) {
                        var currAuthorWidth = authorWidth * (mPoetryLine?.process!! / mPoetryLine?.totalTime!!.toFloat())
                        if (currAuthorWidth > authorWidth) {
                            currAuthorWidth = authorWidth
                        }
                        //Log.v("MYTAG","titleWidth:${titleWidth},currTitleWidth:${currTitleWidth}")
                        val left = authorX
                        val top = authorY - authorHeight
                        val right = authorX + currAuthorWidth
                        val bottom = authorY + authorHeight
                        Log.v("MYTAG", "left:${left},top:${top},right:${right},bottom:${bottom}")
                        canvas.save()
                        canvas.clipRect(RectF(left, top, right, bottom))
                        canvas.drawText(it.name, authorX, authorY, mAuthorPaintRed)
                        canvas.restore()
                    }
                }
                currHeight += PoetryUntil.dpToPx(context, 20f)
                it.content?.let {
                    it.forEachIndexed { index, lineTime ->
                        val contentWidth = PoetryUntil.getTextWidth(mContentPaint, lineTime.label)
                        val contentHeight = PoetryUntil.getRealTextHeight(mContentPaint)

                        val contentX = screenWidth / 2 - PoetryUntil.getTextWidth(mContentPaint, lineTime.label) / 2 + PoetryUntil.dpToPx(context, 15f)
                        val contentY = currHeight
                        canvas.drawText(lineTime.label, contentX, contentY, mContentPaint)

                        if (mPoetryLine?.type == PoetryPlayType.MAINBODY.type && index == mPoetryLine!!.position && mPoetryLine?.process!! > 0 && mPoetryLine?.totalTime!! > 0) {
                            var currAuthorWidth = contentWidth * (mPoetryLine?.process!! / mPoetryLine?.totalTime!!.toFloat())
                            if (currAuthorWidth > contentWidth) {
                                currAuthorWidth = contentWidth
                            }
                            //Log.v("MYTAG","titleWidth:${titleWidth},currTitleWidth:${currTitleWidth}")
                            val left = contentX
                            val top = contentY - contentHeight
                            val right = contentX + currAuthorWidth
                            val bottom = contentY + contentHeight
                            Log.v("MYTAG", "left:${left},top:${top},right:${right},bottom:${bottom}")
                            canvas.save()
                            canvas.clipRect(RectF(left, top, right, bottom))
                            canvas.drawText(lineTime.label, contentX, contentY, mContentPaintRed)
                            canvas.restore()
                        }

                        currHeight = currHeight + PoetryUntil.getTextHeight(mContentPaint) + PoetryUntil.dpToPx(context, 15f)
                    }
                }
            }
        }

    }

    fun initData(poetry: Poetry?) {
        this.mPoetry = poetry
        invalidate()
    }

    fun setCurrProcess(playLine: PoetryLine?) {
        playLine?.let {
            Log.v("MYTAG", "totalTime:${playLine.totalTime},process:${playLine.process},${playLine.process / playLine.totalTime.toFloat()}")
        }
        mPoetryLine = playLine
        invalidate()
    }
}