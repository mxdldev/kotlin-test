package com.example.kotlin

/**
 * Description: <KaraokeActivity><br>
 * Author:      mxdl<br>
 * Date:        2023/8/10<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.view.IncreasingWidthTextView


class Test1Activity : AppCompatActivity() {

    private val fullText = "你" // Your single Chinese character here...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test1)
        findViewById<IncreasingWidthTextView>(R.id.animatedTextView).setTextWithIncreasingWidth(fullText);
        Handler().postDelayed({ findViewById<IncreasingWidthTextView>(R.id.animatedTextView1).setTextWithIncreasingWidth("好"); }, 1500)
        Handler().postDelayed({ findViewById<IncreasingWidthTextView>(R.id.animatedTextView2).setTextWithIncreasingWidth("啊"); }, 3000)
    }

}


