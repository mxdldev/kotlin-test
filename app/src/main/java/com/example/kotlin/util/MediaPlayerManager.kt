package com.example.kotlin.util

import android.os.Handler
import android.os.Looper
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * Description: <MediaPlayerManager><br>
 * Author:      mxdl<br>
 * Date:        2024/3/18<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */


// 根据需要添加更多播放和暂停的时间段
class MediaPlayerScheduler private constructor() {
    private val handler = Handler()
    private lateinit var mediaPlayer: IjkMediaPlayer
    private var stages: List<Pair<Long, Long>> = listOf() // 初始化为空列表
    private var currentStageIndex = 0 // 当前阶段索引
    private var isPaused = false // 当前是否暂停

    companion object {
        @Volatile
        private var instance: MediaPlayerScheduler? = null

        fun getInstance(): MediaPlayerScheduler =
            instance ?: synchronized(this) {
                instance ?: MediaPlayerScheduler().also { instance = it }
            }
    }

    fun setDataSource(path: String) {
        mediaPlayer = IjkMediaPlayer().apply {
            setDataSource(path)
            prepareAsync()
            setOnPreparedListener {
                it.start()
                scheduleNextPhase()
            }
            setOnCompletionListener {
                // 对播放完成进行处理
                handler.removeCallbacksAndMessages(null)
            }
        }
    }

    fun setStages(externalStages: List<Pair<Long, Long>>) {
        stages = externalStages
    }

    private fun scheduleNextPhase() {
        if (currentStageIndex >= stages.size) {
            // 如果所有阶段完成，无需再调度
            return
        }

        val (playTime, pauseTime) = stages[currentStageIndex]

        if (!isPaused) {
            // 进入暂停阶段
            handler.postDelayed({
                mediaPlayer.pause()
                isPaused = true
                handler.postDelayed({ // 安排下一个播放阶段
                    currentStageIndex++
                    scheduleNextPhase()
                }, pauseTime)
            }, playTime)
        } else {
            // 进入播放阶段
            mediaPlayer.start()
            isPaused = false
            scheduleNextPhase()
        }
    }

    fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }
}