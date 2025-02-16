package com.mdubovikov.player.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.mdubovikov.player.domain.entities.TrackPlayer
import com.mdubovikov.theme.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class MusicService : Service() {

    val binder = MusicBinder()

    inner class MusicBinder : Binder() {

        fun getService() = this@MusicService

        fun setMusicList(list: List<TrackPlayer>) {
            this@MusicService.musicList = list.toMutableList()
        }

        fun currentDuration() = this@MusicService.currentDuration

        fun maxDuration() = this@MusicService.maxDuration

        fun isPlaying() = this@MusicService.isPlaying

        fun getCurrentTrack() = this@MusicService.currentTrackPlayer

    }

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var mediaPlayer = MediaPlayer()

    private lateinit var session: MediaSessionCompat

    private val currentTrackPlayer = MutableStateFlow<TrackPlayer?>(null)

    private val maxDuration = MutableStateFlow(0f)
    private val currentDuration = MutableStateFlow(0f)

    private var musicList = mutableListOf<TrackPlayer>()

    private val isPlaying = MutableStateFlow(false)

    private var job: Job? = null

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        session = MediaSessionCompat(this, "music")
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                PREV -> {
                    prev()
                }

                NEXT -> {
                    next()
                }

                PLAY_PAUSE -> {
                    playPause()
                }

                else -> {
                    currentTrackPlayer.update { musicList[0] }
                    currentTrackPlayer.value?.let { track -> play(track) }
                }
            }
        }

        return START_STICKY
    }

    fun prev() {
        job?.cancel()
        mediaPlayer.reset()

        val index = musicList.indexOf(currentTrackPlayer.value)
        val prevIndex = (index - 1 + musicList.size) % musicList.size
        val prevItem = musicList[prevIndex]

        currentTrackPlayer.update { prevItem }

        currentTrackPlayer.value?.id?.let { getUri(it.toString()) }
            ?.let { mediaPlayer.setDataSource(this, it) }
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            currentTrackPlayer.value?.let { track -> sendNotification(track) }
            updateDurations()
        }
    }

    fun next() {
        job?.cancel()
        mediaPlayer.reset()

        val index = musicList.indexOf(currentTrackPlayer.value)
        val nextIndex = index.plus(1).mod(musicList.size)
        val nextItem = musicList[nextIndex]

        currentTrackPlayer.update { nextItem }
        getUri(nextItem.id.toString())?.let { mediaPlayer.setDataSource(this, it) }
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            currentTrackPlayer.value?.let { track -> sendNotification(track) }
            updateDurations()
        }
    }

    fun playPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPlaying.update { false }
        } else {
            mediaPlayer.start()
            isPlaying.update { true }
        }
        currentTrackPlayer.value?.let { sendNotification(it) }
    }

    fun updateDurations() {
        job = serviceScope.launch {
            try {
                maxDuration.update { mediaPlayer.duration.toFloat() }
                while (mediaPlayer.isPlaying) {
                    currentDuration.update { mediaPlayer.currentPosition.toFloat() }
                    delay(1000)
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    private fun play(track: TrackPlayer) {
        mediaPlayer.reset()
        getUri(track.id.toString())?.let { mediaPlayer.setDataSource(this, it) }
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            sendNotification(track)
            updateDurations()
        }
    }

    private fun getUri(source: String): Uri? {
        return when {
            source.startsWith("http://") || source.startsWith("https://") -> {
                Uri.parse(source)
            }

            source.startsWith("/storage") || source.startsWith("/data") -> {
                val file = File(source)
                if (file.exists()) {
                    Uri.fromFile(file)
                } else {
                    null
                }
            }

            else -> {
                Uri.parse("android.resource://${packageName}/${source}")
            }
        }
    }

    private fun sendNotification(track: TrackPlayer) {

        isPlaying.update { mediaPlayer.isPlaying }
        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
            .setMediaSession(session.sessionToken)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setStyle(style)
            .setContentTitle(track.title)
            .setContentText(track.artist)
            .addAction(R.drawable.ic_previous, "prev", createPrevPendingIntent())
            .addAction(
                if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                "play_pause",
                createPlayPausePendingIntent()
            )
            .addAction(R.drawable.ic_next, "next", createNextPendingIntent())
            .setSmallIcon(R.drawable.ic_placeholder)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)

        startForeground(1, notification)
    }

    fun createPrevPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            action = PREV
        }
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun createPlayPausePendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            action = PLAY_PAUSE
        }
        return PendingIntent.getService(
            this, 1, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun createNextPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            action = NEXT
        }
        return PendingIntent.getService(
            this, 2, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        mediaPlayer.stop()
        mediaPlayer.release()
        session.release()
    }

    companion object {
        const val PREV = "prev"
        const val NEXT = "next"
        const val PLAY_PAUSE = "play_pause"
        const val CHANNEL_ID = "1"
        const val CHANNEL_NAME = "Music notification"
    }
}