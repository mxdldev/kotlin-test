package com.example.kotlin

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.entity.Poetry
import com.example.kotlin.entity.PoetryLine
import com.example.kotlin.entity.PoetryPlayListener
import com.example.kotlin.entity.PoetryPlayType
import com.example.kotlin.util.*
import com.example.kotlin.view.PoetryTextView
import com.google.gson.Gson
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil


class PoetryActivity : AppCompatActivity() {
    lateinit var mPoetryTextView: PoetryTextView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry)
        var poetry = findViewById<PoetryTextView>(R.id.view_poetry)
        val data = parseData()
        poetry.initData(data)
        var titleTotalTime = 0L
        var listPoetryLine = ArrayList<PoetryLine>()
        var titleListTime = ArrayList<Int>()
        //遍历标题中的每一个汉字
        data?.poem?.title?.nametime?.forEachIndexed { index, wordTime ->
            //累加标题每一个文字内容的时长
            titleTotalTime += (wordTime.contentEndtime - wordTime.contentStartime)
            if (index != 0) {
                //添加文字头的时长
                titleListTime.add(wordTime.eEndtimeHead - wordTime.eStartimeHead)
            }
            val contentTime = wordTime.contentEndtime - wordTime.contentStartime

            //添加文字内容的时长，要对文正内容时长进行切割
            titleListTime.addAll(divideAndPrint(contentTime, 100))

            //添加文字尾的时长
            titleListTime.add(wordTime.eEndtimeBehind - wordTime.eStartimeBehind)
        }
        listPoetryLine.add(PoetryLine(PoetryPlayType.TITLE.type, 0, titleTotalTime, 0, "http://192.168.186.20${data?.poem?.title?.mp3}", titleListTime))
        var authorTotalTime = 0L
        var authorListTime = ArrayList<Int>()
        data?.poem?.poet?.nametime?.forEachIndexed { index, wordTime ->
            authorTotalTime += (wordTime.contentEndtime - wordTime.contentStartime)
            if (index != 0) {
                authorListTime.add(wordTime.eEndtimeHead - wordTime.eStartimeHead)
            }
            val contentTime = wordTime.contentEndtime - wordTime.contentStartime
            authorListTime.addAll(divideAndPrint(contentTime, 100))
            authorListTime.add(wordTime.eEndtimeBehind - wordTime.eStartimeBehind)
        }
        listPoetryLine.add(PoetryLine(PoetryPlayType.AUTHOR.type, 0, authorTotalTime, 0, "http://192.168.186.20${data?.poem?.poet?.mp3}", authorListTime))

        data?.poem?.content?.forEachIndexed { index, lineTime ->
            var contentTotalTime = 0L
            var contentListTime = ArrayList<Int>()
            lineTime?.lableTime?.forEachIndexed { index, wordTime ->
                if (index != 0) {
                    contentListTime.add(wordTime.eEndtimeHead - wordTime.eStartimeHead)
                }
                val contentTime = wordTime.contentEndtime - wordTime.contentStartime
                contentListTime.addAll(divideAndPrint(contentTime, 100))
                contentListTime.add(wordTime.eEndtimeBehind - wordTime.eStartimeBehind)
                contentTotalTime += (wordTime.contentEndtime - wordTime.contentStartime)
            }
            listPoetryLine.add(PoetryLine(PoetryPlayType.MAINBODY.type, index, contentTotalTime, 0, "http://192.168.186.20${lineTime?.mp3}", contentListTime))
        }
        mPoetryTextView = findViewById(R.id.view_poetry)
        findViewById<Button>(R.id.btn_play).setOnClickListener {
//            PoetryPlayerManager.mPoetryPlayListener = object : PoetryPlayListener {
//                override fun onStart(postion: Int, poetryLine: PoetryLine) {
//
//                }
//
//
//                override fun onProcess(poetryLine: PoetryLine) {
//                    mPoetryTextView.setCurrProcess(poetryLine)
//                }
//
//                override fun onFinish(postion: Int) {
//                    if (postion == listPoetryLine.size - 1) {
//                        mPoetryTextView.setCurrProcess(null)
//                    }
//                }
//
//            }
            test()
            //PoetryPlayerManager.startPlay(listPoetryLine)
        }
    }


    fun divideAndPrint(number: Int, divisor: Int): List<Int> {
        var list = ArrayList<Int>()
        var remainder = number
        while (remainder >= divisor) {
            list.add(divisor)
            remainder -= divisor
        }
        list.add(remainder)
        return list
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

    override fun onDestroy() {
        super.onDestroy()
        //MediaPlayerScheduler.getInstance().release()
        MediaPlayerControl.getInstance(this).stop()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun test() {
        //            val mediaPlayerManager = MediaPlayerManager.getInstance()
        //            // 设置数据源，参数为音频文件的路径
        //            mediaPlayerManager.setDataSource("http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3")
        //            // 开始播放调度，这将启动媒体播放并依据事先设置的时间点暂停和恢复播放
        //            mediaPlayerManager.startPlayScheduling()
        //            val mediaPlayerScheduler = MediaPlayerScheduler.getInstance()
        //            mediaPlayerScheduler.setDataSource("http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3")

        // 从外部设置播放和暂停的阶段
        //            val externalStages = listOf(
        //                3400L-700L to 5 * 1000L,
        //                6700L-3400L-700L to 5 * 1000L,
        //                11760L - 6700L-700L to 5 * 1000L,
        //                15900L - 11760L-700L to 5 * 1000L,
        //                19680L - 15900L-700L to 5 * 1000L,
        //                24100 - 19680L-700L to 5 * 1000L,
        //                // 根据需要增加更多阶段
        //            )
        //            mediaPlayerScheduler.setStages(externalStages)
        //
        //            // 设置数据源并启动播放
        //            mediaPlayerScheduler.setDataSource("http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3")
        //PoetryPlayerManager.startPlay(listPoetryLine, titleListTime)

        //val control = MediaPlayerControl.getInstance(this)
        //val timePoints = listOf(TimePoint(2900-500, 5000),TimePoint(6180-500, 5000), TimePoint(11300-500, 5000), TimePoint(15380-500, 5000), TimePoint(19030-500, 5000), TimePoint(23060-500, 5000))
        //val timePoints1 = listOf(TimePoint(6180-500, 5000), TimePoint(15380-500, 5000), TimePoint(23060-500, 5000))
        //control.setAudioFilePath("http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3", timePoints1)

        //val timePoints2 = listOf(TimePoint(4120, 5000), TimePoint(19940, 5000), TimePoint(31460, 5000), TimePoint(40180, 5000), TimePoint(50780, 5000))
        //control.setAudioFilePath("http://192.168.186.20//pub_cloud/110/2013/0166/shuidiaogetouqw.mp3", timePoints2)
        //control.start()

        //AudioPlayer.getInstance().play("http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3",0,3400)
        //AudioPlayer.getInstance().play("http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3",6700,11760)
        //AudioPlayer.getInstance().play(this@PoetryActivity,"jingyesiqw",5020,10080)
        //AudioPlayer.getInstance().play(this@PoetryActivity,"jingyesiqw",10080,14210)
        //AudioPlayer.getInstance().play(this@PoetryActivity,"jingyesiqw",14210,18000)
        //AudioPlayer.getInstance().play(this@PoetryActivity,"jingyesiqw",5590,14260)
        //AudioPlayer.getInstance().play(this@PoetryActivity,"jingyesiqw",14260,20200)
        //AudioPlayer.getInstance().play(this@PoetryActivity,"jingyesiqw",14260,20200)
        //AudioPlayer.getInstance().play(this@PoetryActivity, "jingyesiqw", 20200, 28260)
        //AudioPlayer1.getInstance().play("file:///android_asset/jingyesiqw.mp3",0,11760)
        //AudioPlayer1.getInstance().play("http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3",6700,11760)
        //AudioPlayer.getInstance().play("http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3",11760,15900)


        val url = "https://jx-file.mypep.cn/pub_cloud/110/2013/0176/qiusiqw.mp3?auth_key=1711616896-0-0-db8393b4f2b41a3a75b9da3061d2b122"
        val url1 = "http://192.168.186.20/pub_cloud/110/2013/0008/jingyesiqw.mp3"
        MP3Downloader.downloadMP3(this, url, object : DownloadListener {
            override fun onDownloadStart() {
            }

            override fun onDownloadProgress(progress: Int) {
                Log.v("MYTAG", "onDownloadProgress start progress:${progress}")
            }

            override fun onDownloadComplete(file: File) {
                //val timestamps = listOf(1700L, 5020L,10080L,14210L,18000L,22500L) // Example timestamps
                val timestamps = listOf(3080L, 5590L, 14260L, 25730L, 28260L, 33570L) // Example timestamps
                val mp3Player = MP3Player.getInstance()
                mp3Player.setPauseTimestamps(timestamps)
                //val mp3File = File(getExternalFilesDir(null), "your_mp3_file.mp3")
                mp3Player.play(file)
            }

            override fun onDownloadError(error: String) {
            }

        })
    }
}