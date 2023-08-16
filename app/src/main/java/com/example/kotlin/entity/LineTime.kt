package com.example.kotlin.entity

/**
 * Description: <Line><br>
 * Author:      mxdl<br>
 * Date:        2023/8/16<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
data class LineTime(val annotation:List<Annotation>,val termOptions:List<TermOption>,val mp3:String,val mask:List<Any>,val line:Int,val label:String,val cutoff:List<Int>,val noVoiceLabel:String,val polyphone:List<Any>,val lableTime:List<WordTime>)
