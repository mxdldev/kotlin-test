package com.example.kotlin.view

/**
 * Description: <CustomTextView><br>
 * Author:      mxdl<br>
 * Date:        2023/8/11<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */


import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    fun setTextWithPartialWidth(text: CharSequence) {
        // Calculate the ratio to adjust text width based on TextView's width
        val widthRatio = width.toFloat() / paint.measureText(text.toString())

        // Create a SpannableString with EllipsizeBeginningSpan
        val spannable = SpannableString(text)
        spannable.setSpan(EllipsizeBeginningSpan(widthRatio), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Set the SpannableString to the TextView
        setText(spannable, BufferType.SPANNABLE)
    }
}
