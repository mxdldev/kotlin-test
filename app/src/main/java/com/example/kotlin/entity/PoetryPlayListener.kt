package com.example.kotlin.entity

/**
 * Description: <PoetryPlayListener><br>
 * Author:      mxdl<br>
 * Date:        2023/8/23<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
interface PoetryPlayListener {
    fun onStart(postion:Int,poetryLine: PoetryLine)
    fun onProcess(poetryLine: PoetryLine)
    fun onFinish(postion:Int)

}