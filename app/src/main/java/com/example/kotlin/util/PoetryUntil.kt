package com.example.kotlin.util

import android.content.Context
import android.graphics.Paint
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

/**
 * Description: <PoetryUntil><br>
 * Author:      mxdl<br>
 * Date:        2023/8/21<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
class PoetryUntil {
    companion object {
        /**
         * 获取真实的歌词高度
         *
         * @param paint
         * @return
         */
        fun getRealTextHeight(paint: Paint): Int {
            val fm = paint.fontMetrics
            return (-fm.leading - fm.ascent + fm.descent).toInt()
        }

        /**
         * 获取行歌词高度。用于y轴位置计算
         *
         * @param paint
         * @return
         */
        fun getTextHeight(paint: Paint): Float{
            val fontMetrics = paint.fontMetrics
            return Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent)
        }

        /**
         * 获取文本宽度
         *
         * @param paint
         * @param text
         * @return
         */
        fun getTextWidth(paint: Paint, text: String): Float {
            return paint.measureText(text)
        }

        fun spToPx(context: Context, sp: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
        }
        fun dpToPx(context: Context, dpValue: Float): Float {
            val displayMetrics = context.resources.displayMetrics
            val density = displayMetrics.density
            return dpValue * density + 0.5f
        }
        fun getScreenWidth(context: Context): Int {
            //获取屏幕宽度
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val displayMetrics = DisplayMetrics()
            display.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }
    }
}