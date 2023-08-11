package com.example.kotlin

/**
 * Description: <TestActivity><br>
 * Author:      mxdl<br>
 * Date:        2023/8/11<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
    private val textView2 by lazy { findViewById<TextView>(R.id.textView2) }
    private val textToAnimate2 = "床"
    private val fadeDuration = 4000L // 渐变持续时间，毫秒
    private val numSegments = 4 // 分割为几份

    private val textView by lazy { findViewById<TextView>(R.id.textView) }
    private val textToAnimate = "窗前前明月光，疑是地上霜，举头望明月，低头思故乡"
    private val fadeInDuration = 1000L // 淡入持续时间，毫秒
    private val delayBetweenChars = 500L // 每个字母之间的延迟，毫秒
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        animateText()
    }

    private fun animateText() {
        if (currentIndex < textToAnimate.length) {
            val currentChar = textToAnimate[currentIndex]

            Handler().postDelayed({
                textView.append(currentChar.toString())
                //textView.animate().alpha(1f).setDuration(fadeInDuration).start()
                currentIndex++
                animateText()
            }, delayBetweenChars)
        }
    }

    private fun animateTextSegments() {
        val char = textToAnimate2[0]
        val spannable = SpannableStringBuilder()

        val segmentWidth = textView.paint.measureText(char.toString()) / numSegments

        for (i in 0 until numSegments) {
            val segment = char.toString()
            val spannableSegment = SpannableString(segment)
            spannableSegment.setSpan(
                ForegroundColorSpan(Color.BLACK),
                0, segment.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )

            val animator = ObjectAnimator.ofArgb(
                spannableSegment.getSpans(0, segment.length, Any::class.java)[0],
                "foregroundColor",
                Color.BLACK,
                Color.RED
            )
            animator.duration = fadeDuration
            animator.startDelay = i * (fadeDuration / numSegments)
            animator.start()

            // Update segment's width to fit the text width
            val end = ((i + 1) * segmentWidth).toInt()
            spannable.replace(0, end, spannableSegment)

            if (i < numSegments - 1) {
                // Add space between segments
                spannable.append(" ")
            }
        }

        textView2.text = spannable
    }
}

