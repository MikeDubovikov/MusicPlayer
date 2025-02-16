package com.mdubovikov.data.database

import android.content.Context
import android.os.Environment
import android.util.Log
import com.mdubovikov.data.network.dto.TrackDto
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class TrackDownloadManager @Inject constructor(
    private val context: Context
) {

    fun download(track: TrackDto, onComplete: (Boolean) -> Unit = {}) {
        val fileName = "${track.id}.mp3"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

        // Если файл уже существует — возвращаем успех
        if (file.exists()) {
            Log.d("TrackManager", "Файл уже существует: ${file.absolutePath}")
            onComplete(true)
            return
        }

        // Скачиваем файл
        val client = OkHttpClient()
        val request = Request.Builder().url(track.remoteUri).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onComplete(false)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    val inputStream = body.byteStream()
                    val outputStream = FileOutputStream(file)

                    try {
                        val buffer = ByteArray(1024)
                        var bytesRead: Int

                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }

                        outputStream.flush()
                        Log.d("TrackManager", "Файл успешно скачан: ${file.absolutePath}")
                        onComplete(true) // Успешное скачивание
                    } catch (e: IOException) {
                        e.printStackTrace()
                        onComplete(false)
                    } finally {
                        inputStream.close()
                        outputStream.close()
                    }
                } ?: onComplete(false)
            }
        })
    }

    fun delete(id: Long, onComplete: (Boolean) -> Unit = {}) {
        val fileName = "$id.mp3"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

        // Проверяем, существует ли файл
        if (file.exists()) {
            if (file.delete()) {
                Log.d("TrackManager", "Файл удален: ${file.absolutePath}")
                onComplete(true) // Успешное удаление
            } else {
                Log.e("TrackManager", "Ошибка при удалении файла.")
                onComplete(false)
            }
        } else {
            Log.e("TrackManager", "Файл не найден: ${file.absolutePath}")
            onComplete(false) // Файл не найден, возвращаем false
        }
    }
}
