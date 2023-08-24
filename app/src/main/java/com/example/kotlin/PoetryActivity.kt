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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil


class PoetryActivity : AppCompatActivity() {
    lateinit var mPoetryTextView: PoetryTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry)
        var poetry = findViewById<PoetryTextView>(R.id.view_poetry)
        val data = parseData()
        poetry.initData(data)
        var titleTotalTime = 0L
        var listPoetryLine = ArrayList<PoetryLine>()
        var titleListTime = ArrayList<Int>()
        data?.poem?.title?.nametime?.forEachIndexed { index, wordTime ->
            titleTotalTime += (wordTime.contentEndtime - wordTime.contentStartime)
            if (index != 0) {
                titleListTime.add(wordTime.eEndtimeHead - wordTime.eStartimeHead)
            }
            val contentTime = wordTime.contentEndtime - wordTime.contentStartime
            repeat(ceil(contentTime / 100f).toInt()) {
                titleListTime.add(100)
            }
            titleListTime.add(wordTime.eEndtimeBehind - wordTime.eStartimeBehind)
        }
        listPoetryLine.add(PoetryLine(PoetryPlayType.TITLE.type, 0, titleTotalTime, 0, "http://192.168.186.20${data?.poem?.title?.mp3}", titleListTime))
        var authorTotalTime = 0L
        var authorListTime = ArrayList<Int>()
        data?.poem?.poet?.nametime?.forEachIndexed { index, wordTime ->
            authorTotalTime += (wordTime.contentEndtime - wordTime.contentStartime)
            authorListTime.add(wordTime.eEndtimeHead - wordTime.eStartimeHead)
            val contentTime = wordTime.contentEndtime - wordTime.contentStartime
            repeat(ceil(contentTime / 100f).toInt()) {
                authorListTime.add(100)
            }
            authorListTime.add(wordTime.eEndtimeBehind - wordTime.eStartimeBehind)
        }
        listPoetryLine.add(PoetryLine(PoetryPlayType.AUTHOR.type, 0, authorTotalTime, 0, "http://192.168.186.20${data?.poem?.poet?.mp3}",authorListTime))

        data?.poem?.content?.forEachIndexed { index, lineTime ->
            var contentTotalTime = 0L
            var contentListTime = ArrayList<Int>()
            lineTime?.lableTime?.forEachIndexed { index, wordTime ->
                contentListTime.add(wordTime.eEndtimeHead - wordTime.eStartimeHead)
                val contentTime = wordTime.contentEndtime - wordTime.contentStartime
                repeat(ceil(contentTime / 100f).toInt()) {
                    contentListTime.add(100)
                }
                contentListTime.add(wordTime.eEndtimeBehind - wordTime.eStartimeBehind)
                contentTotalTime += (wordTime.contentEndtime - wordTime.contentStartime)
            }
            listPoetryLine.add(PoetryLine(PoetryPlayType.MAINBODY.type, index, contentTotalTime, 0, "http://192.168.186.20${lineTime?.mp3}",contentListTime))
        }
        mPoetryTextView = findViewById(R.id.view_poetry)
        findViewById<Button>(R.id.btn_play).setOnClickListener {
            PoetryPlayerManager.mPoetryPlayListener = object : PoetryPlayListener {
                override fun onStart(postion: Int, poetryLine: PoetryLine) {

                }


                override fun onProcess(poetryLine: PoetryLine) {
                    mPoetryTextView.setCurrProcess(poetryLine)
                }

                override fun onFinish(postion: Int) {
                    if (postion == listPoetryLine.size - 1) {
                        mPoetryTextView.setCurrProcess(null)
                    }
                }

            }
            PoetryPlayerManager.startPlay(listPoetryLine, titleListTime)
        }
    }

    fun parseData(): Poetry? {
        val resources: Resources = getResources()
        return try {
            val inputStream: InputStream = resources.openRawResource(R.raw.shi_5jingyesi)
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