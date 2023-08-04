package com.example.kotlin

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.text.style.SuperscriptSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.view.WordClickableSpan


class MainActivity : AppCompatActivity() {

    private var popupWindow: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏标题栏
        supportActionBar?.hide()

        // 隐藏系统状态栏和导航栏
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        setContentView(R.layout.activity_main)

        // 获取显示古诗词的TextView
        val poemTextView = findViewById<TextView>(R.id.poemTextView)

        // 原始古诗词文本
        val poem = "床前明月光，疑①是地上霜。举头望明月，低头思故乡。"

        // 设置要显示角标的文字位置
        val superscriptStart = 6

        // 创建SpannableString并设置角标样式
        val spannableString = SpannableString(poem)
        spannableString.setSpan(SuperscriptSpan(), superscriptStart+1, superscriptStart + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 在"疑"字后插入角标①并设置ClickableSpan
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showPopup(widget, "这是“疑”的解释")
            }

            override fun updateDrawState(ds: TextPaint) {
                // 设置显示效果，这里可以设置下划线颜色等
                ds.color = Color.BLUE
            }
        }
        spannableString.setSpan(clickableSpan, superscriptStart , superscriptStart + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 设置"疑"字的样式，可以增加下划线等效果
        spannableString.setSpan(UnderlineSpan(), superscriptStart , superscriptStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 将SpannableString设置给TextView显示
        poemTextView.text = spannableString
        poemTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showPopup(anchorView: View, explanation: String) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null)
        val popupTextView = popupView.findViewById<TextView>(R.id.popupTextView)
        popupTextView.text = explanation

        popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        // 获取"疑"字在TextView中的位置
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        val x = location[0]
        val y = location[1]

        // 获取"疑"字的宽度
        val textViewLayout = (anchorView as TextView).layout
        val startOffset = textViewLayout.getOffsetForHorizontal(anchorView.lineCount - 1, 0f)
        val endOffset = textViewLayout.getOffsetForHorizontal(anchorView.lineCount - 1, anchorView.width.toFloat())
        val measureText = anchorView.text.subSequence(startOffset, endOffset).toString()
        val textPaint = anchorView.paint
        val width = textPaint.measureText(measureText).toInt()

        // 设置弹窗位置
        popupWindow?.showAtLocation(anchorView, Gravity.NO_GRAVITY, x + width, y - anchorView.height)
    }

}
