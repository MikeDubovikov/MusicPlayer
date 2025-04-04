package com.mdubovikov.player.service

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MusicService : MediaSessionService() {

    private val player by lazy {
        MusicExoPlayer.getInstance(this)
    }

    private val notificationManager by lazy {
        MediaNotificationManager(this, player)
    }

    private val mediaSession by lazy {
        MediaSession.Builder(this, player).build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        notificationManager.startNotificationService(
            mediaSessionService = this,
            mediaSession = mediaSession
        )

        return START_NOT_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    @OptIn(UnstableApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {
        pauseAllPlayersAndStopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.run {
            player.release()
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
        MusicExoPlayer.release()
    }
}