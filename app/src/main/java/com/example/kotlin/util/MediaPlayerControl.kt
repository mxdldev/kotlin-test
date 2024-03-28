package com.example.kotlin.util

/**
 * Description: <MediaPlayerControl><br>
 * Author:      mxdl<br>
 * Date:        2024/3/18<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.media.MediaPlayer
import android.os.Handler
import android.content.Context
import android.os.Looper
import android.util.Log
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class MediaPlayerControl private constructor(private val context: Context) {
    private var mediaPlayer: IjkMediaPlayer? = null
    private val handler = Handler(Looper.myLooper()!!)
    private var timePoints: List<TimePoint> = listOf()
    private var currentIndex = 0
    private var isPaused = false

    companion object {
        @Volatile private var instance: MediaPlayerControl? = null

        fun getInstance(context: Context): MediaPlayerControl = instance ?: synchronized(this) {
            instance ?: MediaPlayerControl(context.applicationContext).also { instance = it }
        }
    }

    fun setAudioFilePath(audioFilePath: String, timePoints: List<TimePoint>) {
        this.timePoints = timePoints
        mediaPlayer?.release()
        mediaPlayer = IjkMediaPlayer().apply {
            setDataSource(audioFilePath)
            prepareAsync()
        }
    }

    fun start() {
        resetPlayback()
        mediaPlayer?.start()
        checkPosition()
    }

    private fun checkPosition() {
        val currentPosition = mediaPlayer?.currentPosition ?: 0
        if (currentIndex < timePoints.size && !isPaused) {
            val currentPoint = timePoints[currentIndex]
            if (currentPosition >= currentPoint.startTime) {
                Log.v("MYTAG3","currentPosition:${currentPosition},currentPoint.startTime: ${currentPoint.startTime}")
                mediaPlayer?.pause()
                isPaused = true
                handler.postDelayed({
                    mediaPlayer?.start()
                    isPaused = false
                    currentIndex++
                    checkPosition()
                }, currentPoint.duration)
            } else {
                handler.postDelayed({ checkPosition() }, 100)
            }
        }
    }

    private fun resetPlayback() {
        currentIndex = 0
        isPaused = false
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }
}

data class TimePoint(val startTime: Int, val duration: Long)