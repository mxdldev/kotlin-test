package com.example.kotlin

/**
 * Description: <GradientColorActivity><br>
 * Author:      mxdl<br>
 * Date:        2023/8/10<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GradientColorActivity : AppCompatActivity() {
    private lateinit var gradientTextView:TextView
    private val text = "床"
    private val displayDuration = 2000L // 显示的总时间，毫秒
    private val interval = 50L // 更新间隔，毫秒

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_karaoke)
        gradientTextView = findViewById(R.id.lyricTextView)

        handler = Handler()

        startDisplayAnimation()
    }

    private fun startDisplayAnimation() {
        val totalChars = text.length
        val spannableBuilder = SpannableStringBuilder(text)

        val stepCount = displayDuration / interval

        runnable = object : Runnable {
            var currentStep = 0

            override fun run() {
                if (currentStep <= stepCount) {
                    val progress = currentStep.toFloat() / stepCount.toFloat()
                    val charsToShow = (totalChars * progress).toInt()

                    spannableBuilder.setSpan(
                        ForegroundColorSpan(gradientTextView.currentTextColor),
                        charsToShow,
                        totalChars,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    gradientTextView.text = spannableBuilder

                    currentStep++
                    if (currentStep <= stepCount) {
                        handler.postDelayed(this, interval)
                    }
                }
            }
        }

        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
    }

