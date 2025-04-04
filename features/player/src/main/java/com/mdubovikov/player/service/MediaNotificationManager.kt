package com.mdubovikov.player.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.mdubovikov.theme.R
import javax.inject.Inject

class MediaNotificationManager @Inject constructor(
    private val context: Context,
    private val player: ExoPlayer
) {

    private var notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    fun startNotificationService(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession
    ) {
        buildNotification(mediaSession)
        startForegroundNotification(mediaSessionService)
    }

    @OptIn(UnstableApi::class)
    private fun buildNotification(mediaSession: MediaSession) {
        PlayerNotificationManager.Builder(context, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
            .setMediaDescriptionAdapter(
                MediaNotificationAdapter(
                    context = context,
                    pendingIntent = mediaSession.sessionActivity
                )
            )
            .setSmallIconResourceId(R.drawable.ic_music_note)
            .build()
            .also {
                it.setMediaSessionToken(mediaSession.platformToken)
                it.setUseFastForwardActionInCompactView(true)
                it.setUseRewindActionInCompactView(true)
                it.setUseNextActionInCompactView(false)
                it.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                it.setPlayer(player)
            }
    }

    private fun startForegroundNotification(mediaSessionService: MediaSessionService) {
        val notification = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_ID = 100
        private const val NOTIFICATION_CHANNEL_NAME = "Player notification"
        private const val NOTIFICATION_CHANNEL_ID = "1"
    }
}