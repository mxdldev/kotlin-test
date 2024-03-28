package com.example.kotlin.util

/**
 * Description: <AudioPlayer1><br>
 * Author:      mxdl<br>
 * Date:        2024/3/27<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.os.Handler
import android.util.Log
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException

class AudioPlayer1 private constructor() {
    private var mediaPlayer: IjkMediaPlayer? = null
    private var currentPosition: Long = 0
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var isPlaying: Boolean = false
    private var handler: Handler = Handler()

    companion object {
        private var instance: AudioPlayer1? = null

        fun getInstance(): AudioPlayer1 {
            if (instance == null) {
                instance = AudioPlayer1()
            }
            return instance!!
        }
    }

    fun play(url: String, startTime: Long, endTime: Long) {
        if (mediaPlayer == null) {
            mediaPlayer = IjkMediaPlayer()
        } else {
            mediaPlayer?.reset()
        }

        try {
            mediaPlayer?.dataSource = url
            mediaPlayer?.prepareAsync()

            mediaPlayer?.setOnPreparedListener {
                it.start()
                it.seekTo(startTime.toLong())
                this.startTime = startTime
                this.endTime = endTime
                isPlaying = true
                currentPosition = startTime
                handler.postDelayed(updateProgress, 100)
            }

            mediaPlayer?.setOnErrorListener { mp, what, extra ->
                // Handle errors
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
        handler.removeCallbacks(updateProgress)
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        handler.removeCallbacks(updateProgress)
    }

    fun isPlaying(): Boolean {
        return isPlaying
    }

    private val updateProgress: Runnable = object : Runnable {
        override fun run() {
            Log.v("MYTAG","startTime:${startTime},endTime:${endTime},currentPosition:${currentPosition}")
            if (mediaPlayer != null && mediaPlayer!!.isPlaying && currentPosition < endTime) {
                currentPosition = mediaPlayer!!.currentPosition
                handler.postDelayed(this, 100)
            } else {
                mediaPlayer?.pause()
                isPlaying = false
                handler.removeCallbacks(this)
            }
        }
    }
}
