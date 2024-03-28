package com.example.kotlin.util

/**
 * Description: <MP3Downloader><br>
 * Author:      mxdl<br>
 * Date:        2024/3/28<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*

interface DownloadListener {
    fun onDownloadStart()
    fun onDownloadProgress(progress: Int)
    fun onDownloadComplete(file: File)
    fun onDownloadError(error: String)
}

object MP3Downloader {

    fun downloadMP3(context: Context, url: String, listener: DownloadListener?) {
        listener?.onDownloadStart()

        GlobalScope.launch(Dispatchers.IO) {
            val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            externalFilesDir?.mkdirs()
            val fileName = "downloaded_mp3_${System.currentTimeMillis()}.mp3"
            val outputFile = File(externalFilesDir, fileName)

            try {
                val connection = java.net.URL(url).openConnection()
                connection.connect()
                val fileLength = connection.contentLength

                val inputStream: InputStream = BufferedInputStream(connection.getInputStream())
                val outputStream: OutputStream = FileOutputStream(outputFile)

                val data = ByteArray(1024)
                var total: Long = 0
                var count: Int

                while (inputStream.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    outputStream.write(data, 0, count)

                    val progress = ((total * 100) / fileLength).toInt()
                    listener?.onDownloadProgress(progress)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()

                listener?.onDownloadComplete(outputFile)
            } catch (e: IOException) {
                Log.e("MP3Downloader", "Error downloading MP3: ${e.message}")
                listener?.onDownloadError(e.message ?: "Unknown error")
            }
        }
    }
}
