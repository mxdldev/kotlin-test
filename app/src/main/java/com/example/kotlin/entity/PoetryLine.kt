package com.example.kotlin.entity

/**
 * Description: <PoetryLine><br>
 * Author:      mxdl<br>
 * Date:        2023/8/23<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
data class PoetryLine(val type:Int,val position:Int,var totalTime:Long,var process:Long,val mp3url:String,val delayTime:List<Int>)
