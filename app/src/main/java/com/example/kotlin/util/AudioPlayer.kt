package com.example.kotlin.util

/**
 * Description: <AudioPlayer><br>
 * Author:      mxdl<br>
 * Date:        2024/3/27<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi

class AudioPlayer private constructor() {
    private var mediaPlayer: MediaPlayer? = null
    private var currentPosition: Int = 0
    private var startTime: Int = 0
    private var endTime: Int = 0
    private var isPlaying: Boolean = false
    private var handler: Handler = Handler(Looper.myLooper()!!)

    companion object {
        private var instance: AudioPlayer? = null

        fun getInstance(): AudioPlayer {
            if (instance == null) {
                instance = AudioPlayer()
            }
            return instance!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun play(context: Context, url: String, startTime: Int, endTime: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
        mediaPlayer?.setOnPreparedListener {

            this.startTime = startTime
            this.endTime = endTime
            isPlaying = true
            currentPosition = startTime
            it.seekTo(startTime)
            it.start()

            handler.postDelayed(updateProgress, 10)
        }
        mediaPlayer?.setOnErrorListener(MediaPlayer.OnErrorListener { mp, what, extra ->
            Log.e("MYTAG", "onError extra:${extra.toString()},what:${what}")
            false
        })
        val resourceId = context.resources.getIdentifier("qiusiqw", "raw", context.packageName)
        mediaPlayer?.setDataSource(context.resources.openRawResourceFd(resourceId))
        mediaPlayer?.prepareAsync()
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

    val updateProgress: Runnable = object : Runnable {
        override fun run() {
            Log.v("MYTAG", "startTime:${startTime},endTime:${endTime},currentPosition:${currentPosition}")
            currentPosition = mediaPlayer!!.currentPosition
            //if (mediaPlayer != null && mediaPlayer!!.isPlaying && currentPosition < endTime) {
            if (mediaPlayer != null && isPlaying && currentPosition < endTime) {
                handler.postDelayed(this, 10)
            } else {
                mediaPlayer?.pause()
                isPlaying = false
                handler.removeCallbacks(this)
            }
        }
    }
}