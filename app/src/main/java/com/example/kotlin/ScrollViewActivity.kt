package com.example.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin.view.ScrollableTextView

class ScrollViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)
        var scrollableTextView = findViewById<ScrollableTextView>(R.id.view_text)
        val textList = ArrayList<String>()
        textList.add("以下是使用Kotlin实现的示例代码111")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码")
        textList.add("以下是使用Kotlin实现的示例代码222")
        scrollableTextView.setTextList(textList)
    }
}