package com.mdubovikov.player.service

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer

object MusicExoPlayer {
    private var instance: ExoPlayer? = null

    fun getInstance(context: Context): ExoPlayer {
        if (instance == null) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build()

            instance = ExoPlayer.Builder(context)
                .setAudioAttributes(audioAttributes, true)
                .build()
        }
        return instance!!
    }



    fun release() {
        instance?.release()
        instance = null
    }
}
