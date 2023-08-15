package com.example.kotlin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View

/**
 * Description: <MainLineHeightLightView><br>
 * Author:      mxdl<br>
 * Date:        2023/8/15<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
class MainLineHeightLightTextView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    val listText = listOf("窗前明月光", "疑是地上霜", "举头望明月", "低头思故乡")
    var initState = true
    var textWidth = 30
    var textTime = 2000
    var process = 0
    var allProcess = 600
    var index = 0
    var myHandler = object :Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            process += 10
            if(process <= allProcess){
                invalidate()
            }
            index++
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        if (initState) {
            var x = 0f;
            var y = 0f;
            val paint = Paint()
            paint.color = Color.parseColor("#000000")
            paint.textSize = 80f
            listText.forEach {
                y += 100
                canvas.drawText(it, x, y, paint)
            }
            initState = false
            myHandler.sendMessageDelayed(Message(),2000)
        }else{
            val paint = Paint()
            paint.color = Color.parseColor("#000000")
            paint.textSize = 80f

            canvas.drawText("窗前明月光",0f,100f,paint)
            canvas.clipRect(Rect(0,100,process,40))

            val paint1 = Paint()
            paint1.color = Color.parseColor("#ff0000")
            paint1.textSize = 80f
            canvas.drawText("窗前明月光",0f,100f,paint1)
            myHandler.sendMessageDelayed(Message(),200)
        }
        canvas.restore()

    }
}