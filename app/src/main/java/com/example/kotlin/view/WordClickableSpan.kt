package com.example.kotlin.view

/**
 * Description: <WordClickableSpan><br>
 * Author:      mxdl<br>
 * Date:        2023/8/4<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.content.Context
import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.PopupWindow

class WordClickableSpan(private val context: Context, private val explanation: String) : ClickableSpan() {

    override fun onClick(widget: View) {
        // 在这里处理点击事件，弹出解释的弹出窗口
        val popupWindow = PopupWindow(context)
        // 设置popupWindow的内容和样式，这里需要根据你的需求进行实现
        // ...
        popupWindow.showAsDropDown(widget)
    }

    override fun updateDrawState(ds: TextPaint) {
        // 设置显示效果，这里可以设置下划线颜色等
        ds.color = Color.BLUE
    }
}
