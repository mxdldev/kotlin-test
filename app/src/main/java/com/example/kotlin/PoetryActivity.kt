package com.example.kotlin

import android.content.Intent
import android.content.res.Resources
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.entity.Poetry
import com.example.kotlin.entity.PoetryPlayType
import com.example.kotlin.view.PoetryTextView
import com.google.gson.Gson
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.InputStream
import java.io.InputStreamReader


class PoetryActivity : AppCompatActivity() {
    lateinit var myHandler: Handler
    lateinit var mMediaPlayer: IjkMediaPlayer
    lateinit var mPoetryTextView: PoetryTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry)
        var poetry = findViewById<PoetryTextView>(R.id.view_poetry)
        val data = parseData()
        poetry.initData(data)
        mPoetryTextView = findViewById(R.id.view_poetry)
        findViewById<Button>(R.id.btn_play).setOnClickListener {
            val url = "http://192.168.186.20${data?.poem?.title?.mp3}"
            //Log.v("MYTAG", "url:${url}")
            //val url1 = "https://rjdduploadw.mypep.cn/poetry/29.mp3"
            playNetMusic(url)

        }
        myHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val currentPosition = mMediaPlayer.currentPosition
                //Log.v("MYTAG", "currentPosition:$currentPosition")
                var totalTime = 0L
                data?.poem?.title?.nametime?.forEach {
                    totalTime += (it.wordEndtime - it.wordStartime)
                }
                mPoetryTextView.setCurrProcess(PoetryPlayType.TITLE,totalTime,currentPosition)
                myHandler.sendEmptyMessageDelayed(0x01, 100)
            }
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

    private fun playNetMusic(url: String) {
        mMediaPlayer = IjkMediaPlayer()
        //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer.setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener {})
        mMediaPlayer.setOnCompletionListener(IMediaPlayer.OnCompletionListener {
            Log.v("MYTAG", "play finish...")
            myHandler.removeCallbacksAndMessages(null)
        })
        mMediaPlayer.setOnErrorListener(IMediaPlayer.OnErrorListener { mp, what, extra -> //发送播放错误广播
            Log.v("MYTAG", "play error:${mp},${extra}")
            myHandler.removeCallbacksAndMessages(null)
            false
        })
        mMediaPlayer.setOnPreparedListener(IMediaPlayer.OnPreparedListener {
            Log.v("MYTAG", "play start...")
            myHandler.sendEmptyMessage(0x01)
        })
        mMediaPlayer.setSpeed(1.0f)
        mMediaPlayer.setDataSource(url)
        mMediaPlayer.prepareAsync()
    }
}