package com.example.kotlin

import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.entity.Poetry
import com.example.kotlin.entity.PoetryLine
import com.example.kotlin.entity.PoetryPlayListener
import com.example.kotlin.entity.PoetryPlayType
import com.example.kotlin.util.PoetryPlayerManager
import com.example.kotlin.view.PoetryTextView
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader


class PoetryActivity : AppCompatActivity() {
    lateinit var mPoetryTextView: PoetryTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry)
        var poetry = findViewById<PoetryTextView>(R.id.view_poetry)
        val data = parseData()
        poetry.initData(data)
        var titleTotalTime = 0L
        data?.poem?.title?.nametime?.forEach {
            titleTotalTime += (it.wordEndtime - it.wordStartime)
        }
        var authorTotalTime = 0L
        data?.poem?.poet?.nametime?.forEach {
            authorTotalTime += (it.wordEndtime - it.wordStartime)
        }
        var listPoetryLine = ArrayList<PoetryLine>()
        listPoetryLine.add(PoetryLine(PoetryPlayType.TITLE.type, 0, titleTotalTime, 0, "http://192.168.186.20${data?.poem?.title?.mp3}"))
        listPoetryLine.add(PoetryLine(PoetryPlayType.AUTHOR.type, 0, authorTotalTime, 0, "http://192.168.186.20${data?.poem?.poet?.mp3}"))
        data?.poem?.content?.forEachIndexed { index, lineTime ->
            var contentTotalTime = 0L
            lineTime?.lableTime?.forEach {
                contentTotalTime += (it.wordEndtime - it.wordStartime)
            }
            listPoetryLine.add(PoetryLine(PoetryPlayType.MAINBODY.type, index, contentTotalTime, 0, "http://192.168.186.20${lineTime?.mp3}"))
        }
        mPoetryTextView = findViewById(R.id.view_poetry)
        findViewById<Button>(R.id.btn_play).setOnClickListener {
            PoetryPlayerManager.mPoetryPlayListener = object : PoetryPlayListener {
                override fun onStart(postion: Int) {
                }

                override fun onProcess(poetryLine: PoetryLine) {
                    mPoetryTextView.setCurrProcess(poetryLine)
                }

                override fun onFinish(postion: Int) {
                    if(postion == listPoetryLine.size - 1){
                        mPoetryTextView.setCurrProcess(null)
                    }
                }

            }
            PoetryPlayerManager.startPlay(listPoetryLine)
        }
    }

    fun parseData(): Poetry? {
        val resources: Resources = getResources()
        return try {
            val inputStream: InputStream = resources.openRawResource(R.raw.data)
            val reader = InputStreamReader(inputStream)
            val gson = Gson()
            val poetry = gson.fromJson(reader, Poetry::class.java)
            reader.close()
            inputStream.close()
            poetry
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}