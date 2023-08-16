package com.example.kotlin.entity

/**
 * Description: <Poetry><br>
 * Author:      mxdl<br>
 * Date:        2023/8/16<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
data class Poetry(val id: String, val author: Author, val wholeMp3: PoetryMp3, val mp4src: PoetryMp4, val background: String, val poem: PoetryTime, val poetry: Paraphrase, val appreciate: Appreciate, val isPhonetic: Boolean, val isorder: Boolean, val poemDuring: List<Int>)
