package com.mdubovikov.player.service

import android.annotation.SuppressLint
import android.app.Application
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaServiceHandler private constructor(application: Application) : Player.Listener {

    private val player by lazy {
        MusicExoPlayer.getInstance(application)
    }

    private val _mediaState = MutableStateFlow<PlayerMediaState>(PlayerMediaState.Initial)
    val mediaState = _mediaState.asStateFlow()

    private var job: Job? = null

    private var isRepeatTrack = false

    init {
        player.addListener(this)
        job = Job()
    }

    fun addMediaItem(mediaItem: MediaItem) {
        if (!isSameTrack(mediaItem.mediaId)) {
            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
    }

    fun addMediaItemList(mediaItemList: List<MediaItem>) {
        player.setMediaItems(mediaItemList)
        player.prepare()
        player.play()
    }

    fun repeatTrack(): Boolean {
        isRepeatTrack = !isRepeatTrack
        return isRepeatTrack
    }

    private fun isSameTrack(newTrackId: String): Boolean =
        player.currentMediaItem?.mediaId == newTrackId

    private suspend fun playPauseTrack() {
        if (player.playbackState == ExoPlayer.STATE_ENDED) {
            player.seekTo(0)
            player.prepare()
        }

        if (player.isPlaying) {
            player.pause()
            stopProgressUpdate()
        } else {
            player.play()
            _mediaState.value = PlayerMediaState.Playing(isPlaying = player.isPlaying)
            startProgressUpdate()
        }
    }

    suspend fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            is PlayerEvent.Backward -> {
                val newPosition = maxOf(
                    player.currentPosition - playerEvent.newProgress,
                    0
                )
                player.seekTo(newPosition)
            }

            is PlayerEvent.Forward -> {
                val newPosition = minOf(
                    player.currentPosition + playerEvent.newProgress,
                    player.duration
                )
                player.seekTo(newPosition)
            }

            PlayerEvent.PlayPause -> playPauseTrack()
            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> player.seekTo(playerEvent.newProgress)
        }
    }

    @SuppressLint("SwitchIntDef")
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> {
                _mediaState.value =
                    PlayerMediaState.Buffering(player.currentPosition)
            }

            ExoPlayer.STATE_READY -> {
                _mediaState.value = PlayerMediaState.Ready(
                    duration = player.duration,
                    isRepeat = isRepeatTrack
                )
            }

            ExoPlayer.STATE_ENDED -> {
                if (isRepeatTrack) {
                    player.seekTo(0)
                    _mediaState.value = PlayerMediaState.Playing(isPlaying = player.isPlaying)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _mediaState.value = PlayerMediaState.Playing(isPlaying = player.isPlaying)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            _mediaState.value = PlayerMediaState.Progress(
                progress = player.currentPosition,
                duration = player.duration,
                isPlaying = player.isPlaying,
                isRepeat = isRepeatTrack
            )
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _mediaState.value = PlayerMediaState.Playing(isPlaying = player.isPlaying)
    }

    companion object {
        @Volatile
        private var INSTANCE: MediaServiceHandler? = null

        fun getInstance(application: Application): MediaServiceHandler {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MediaServiceHandler(application).also { INSTANCE = it }
            }
        }
    }
}

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    data class Backward(val newProgress: Long) : PlayerEvent()
    data class Forward(val newProgress: Long) : PlayerEvent()
    object Stop : PlayerEvent()
    data class UpdateProgress(val newProgress: Long) : PlayerEvent()
}

sealed class PlayerMediaState {
    object Initial : PlayerMediaState()
    data class Ready(
        val duration: Long,
        val isRepeat: Boolean
    ) : PlayerMediaState()
    data class Progress(
        val progress: Long,
        val duration: Long,
        val isPlaying: Boolean,
        val isRepeat: Boolean
    ) : PlayerMediaState()
    data class Buffering(val progress: Long) : PlayerMediaState()
    data class Playing(val isPlaying: Boolean) : PlayerMediaState()
}