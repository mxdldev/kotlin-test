package com.example.kotlin.view

/**
 * Description: <AnimatedWidthTextView><br>
 * Author:      mxdl<br>
 * Date:        2023/8/11<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView

class AnimatedWidthTextView : AppCompatTextView {

    private val fullText = "你" // Your Chinese text here...

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun animateText() {
        val animator = ValueAnimator.ofInt(0, fullText.length)
        animator.duration = 3000 // Set the duration of the animation
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            val displayedText = fullText.substring(0, animatedValue)
            text = displayedText

            val textWidth = paint.measureText(displayedText).toInt()
            Log.v("MYTAG","textWidth:${textWidth}")
            val layoutParams = layoutParams
            layoutParams.width = textWidth
            setLayoutParams(layoutParams)
        }
        animator.start()
    }
}
