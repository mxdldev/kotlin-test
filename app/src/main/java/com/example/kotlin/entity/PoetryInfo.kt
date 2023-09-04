package com.example.kotlin.entity

/**
 * Description: <PoetryInfo><br>
 * Author:      mxdl<br>
 * Date:        2023/8/28<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
data class PoetryInfo(val id:String,val book_id:String,val chapter_id:String,val title:String,val first_sentence:String,val writer:String,val dynasty_name:String,val genre:Int,val genre_name:String,val index_path:String,val audio_path:String,val img_url:String)