package com.example.kotlin.util

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.kotlin.entity.PoetryLine
import com.example.kotlin.entity.PoetryPlayListener
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.util.*

/**
 * Description: <PoetryPlayerManager><br>
 * Author:      mxdl<br>
 * Date:        2023/8/23<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
object PoetryPlayerManager {
    var mListData: List<PoetryLine>? = null
    lateinit var mMyHandler: Handler
    lateinit var mMediaPlayer: IjkMediaPlayer
    var mCurrPostion = 0
    var mDelayPostion = 0
    var mPoetryPlayListener: PoetryPlayListener? = null
    var mlistDelay: List<Int>? = null

    init {
        mMediaPlayer = IjkMediaPlayer()
        mMediaPlayer.setOnSeekCompleteListener(IMediaPlayer.OnSeekCompleteListener {})
        mMediaPlayer.setOnCompletionListener(IMediaPlayer.OnCompletionListener {
            Log.v("MYTAG", "play finish...")
            mPoetryPlayListener?.onFinish(mCurrPostion)
            mListData?.let {
                if (mCurrPostion <= it.size - 2) {
                    startPlay(++mCurrPostion)
                } else {
                    mMyHandler.removeCallbacksAndMessages(null)
                }
            }

        })
        mMediaPlayer.setOnErrorListener(IMediaPlayer.OnErrorListener { mp, what, extra -> //发送播放错误广播
            Log.v("MYTAG", "play error:${mp},${extra}")
            mMyHandler.removeCallbacksAndMessages(null)
            false
        })
        mMediaPlayer.setOnPreparedListener(IMediaPlayer.OnPreparedListener {
            Log.v("MYTAG", "play start...")
            mDelayPostion = 0
            mlistDelay = mListData!!.get(mCurrPostion).delayTime
            if(mDelayPostion < mlistDelay!!.size){
                mMyHandler.sendEmptyMessageDelayed(0x01, mlistDelay?.get(mDelayPostion)?.toLong()!!)
                mDelayPostion++
            }
            mPoetryPlayListener?.onStart(mCurrPostion, mListData!!.get(mCurrPostion))
        })
        mMediaPlayer.setSpeed(1.0f)
        mMyHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var poetryLine = mListData?.get(mCurrPostion)
                poetryLine?.process = mMediaPlayer.currentPosition
                mPoetryPlayListener?.onProcess(poetryLine!!)
                Log.v("MYTAG1","mDelayPostion:${mDelayPostion}")
                if(mDelayPostion < mlistDelay!!.size){
                    mMyHandler.sendEmptyMessageDelayed(0x01, mlistDelay?.get(mDelayPostion)?.toLong()!!)
                    mDelayPostion++
                }
            }
        }
    }

    fun startPlay(list: List<PoetryLine>, listDelay: List<Int>) {
        mlistDelay = listDelay
        mCurrPostion = 0
        mDelayPostion = 0
        mListData = list
        mMediaPlayer.reset()
        mMediaPlayer.setDataSource(list.get(mCurrPostion).mp3url)
        mMediaPlayer.prepareAsync()
    }

    fun startPlay(position: Int) {
        mCurrPostion = position
        mMediaPlayer.reset()
        mMediaPlayer.setDataSource(mListData?.get(mCurrPostion)?.mp3url)
        mMediaPlayer.prepareAsync()
    }
}