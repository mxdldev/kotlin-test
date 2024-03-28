package com.example.kotlin.util

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File

/**
 * Description: <MP3Player><br>
 * Author:      mxdl<br>
 * Date:        2024/3/20<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */

data class PoetryPlayLine(val lineNumber: Int, val text: String)

interface MP3PlayerListener {
    fun onPlayStart()
    fun onPlayProgress(progress: PoetryPlayLine)
    fun onPlayPause()
    fun onPlayResumed()
    fun onPlayEnd()
    fun onPlayError()
}

class MP3Player private constructor() {

    private var mediaPlayer: MediaPlayer? = null
    private var progressHandler: Handler? = null
    private var listener: MP3PlayerListener? = null
    private var cachedPositions: MutableList<Int> = mutableListOf()
    private var pauseTimestamps: List<Long>? = null
    private var currentPauseTimestampIndex = 0

    private val updateProgressAction: Runnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let { player ->
                val position = player.currentPosition
                Log.v("MYTAG", "position:${position}")
                listener?.onPlayProgress(PoetryPlayLine(0, "Dummy Text")) // Replace with actual line information
                cachedPositions.add(position)

                // Check if current position reaches the pause timestamp
                if (pauseTimestamps != null && currentPauseTimestampIndex < pauseTimestamps!!.size) {
                    val nextPauseTimestamp = pauseTimestamps!![currentPauseTimestampIndex]
                    if (position >= nextPauseTimestamp) {
                        // Pause playback
                        pause()
                        return
                    }
                }

                progressHandler?.postDelayed(this, 100)
            }
        }
    }

    fun setPauseTimestamps(timestamps: List<Long>) {
        pauseTimestamps = timestamps.sorted()
    }

    fun setListener(listener: MP3PlayerListener) {
        this.listener = listener
    }

    fun play(mp3File: File) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(mp3File.absolutePath)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
                listener?.onPlayStart()
                progressHandler?.postDelayed(updateProgressAction, 100)
            }
            mediaPlayer?.setOnCompletionListener {
                stop()
            }
            mediaPlayer?.setOnErrorListener { _, _, _ ->
                listener?.onPlayError()
                stop()
                true
            }
            progressHandler = Handler(Looper.getMainLooper())
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        progressHandler?.removeCallbacks(updateProgressAction)
        listener?.onPlayPause()
        sendCachedPositions()
    }

    private fun sendCachedPositions() {
//        val iterator = cachedPositions.iterator()
//        val delay = 100L // Delay between sending each position
//        var lastPositionSent = 0
//        while (iterator.hasNext()) {
//            val position = iterator.next()
//            val delayTime = position - lastPositionSent
//            Log.v("MYTAG","position:${position},delayTime:${delayTime}")
//            if (delayTime > 0) {
//                progressHandler?.postDelayed({
//                    listener?.onPlayProgress(PoetryPlayLine(0, "Dummy Text")) // Replace with actual line information
//                    lastPositionSent = position
//                    if (!iterator.hasNext()) {
//                        // If no more positions left, resume playback
//                        cachedPositions.clear()
//                        resume()
//                    }
//                }, delayTime.toLong())
//                break
//            } else {
//                listener?.onPlayProgress(PoetryPlayLine(0, "Dummy Text")) // Replace with actual line information
//                lastPositionSent = position
//                iterator.remove()
//                if (!iterator.hasNext()) {
//                    // If no more positions left, resume playback
//                    cachedPositions.clear()
//                    resume()
//                }
//            }
//        }
        if (!cachedPositions.isEmpty()) {
            cachedPositions.forEachIndexed { index, progress ->
                progressHandler?.postDelayed({
                    listener?.onPlayProgress(PoetryPlayLine(0, "Dummy Text"))
                    if (index == cachedPositions.size - 1) {
                        cachedPositions.clear()
                        currentPauseTimestampIndex++
                        resume()
                    }
                }, index * 100L)
            }
        }
    }

    fun resume() {
        mediaPlayer?.start()
        progressHandler?.postDelayed(updateProgressAction, 100)
        listener?.onPlayResumed()
    }

    fun stop() {
        mediaPlayer?.let {
            it.stop()
            it.release()
            mediaPlayer = null
            progressHandler?.removeCallbacks(updateProgressAction)
            progressHandler = null
            cachedPositions.clear()
            listener?.onPlayEnd()
        }
    }

    companion object {
        private var instance: MP3Player? = null
        fun getInstance(): MP3Player {
            if (instance == null) {
                synchronized(MP3Player::class.java) {
                    if (instance == null) {
                        instance = MP3Player()
                    }
                }
            }
            return instance!!
        }
    }

}
