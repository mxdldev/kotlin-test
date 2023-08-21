package com.example.kotlin

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin.entity.Poetry
import com.example.kotlin.view.PoetryTextView
import com.google.gson.Gson
import java.io.InputStream
import java.io.InputStreamReader


class PoetryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poetry)
        var poetry = findViewById<PoetryTextView>(R.id.view_poetry)
        poetry.initData(parseData())
    }

    fun parseData(): Poetry? {
        val resources: Resources = getResources()
        return try {
            val inputStream: InputStream = resources.openRawResource(R.raw.data)
            val reader = InputStreamReader(inputStream)
            val gson = Gson()
            val poetry = gson.fromJson(reader, Poetry::class.java)
            reader.close()
            inputStream.close()
            poetry
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}