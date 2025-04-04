package com.mdubovikov.player.presentation

import android.annotation.SuppressLint
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.mdubovikov.player.domain.GetTrackPlayerUseCase
import com.mdubovikov.player.domain.SwitchStatusTrackPlayerUseCase
import com.mdubovikov.player.domain.entities.TrackPlayer
import com.mdubovikov.player.service.MediaServiceHandler
import com.mdubovikov.player.service.PlayerEvent
import com.mdubovikov.player.service.PlayerMediaState
import com.mdubovikov.util.Container
import com.mdubovikov.util.ViewModelAssistedFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PlayerViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val mediaServiceHandler: MediaServiceHandler,
    private val getTrackPlayerUseCase: GetTrackPlayerUseCase,
    private val switchStatusTrackPlayerUseCase: SwitchStatusTrackPlayerUseCase
) : ViewModel() {

    @AssistedFactory
    interface Factory : ViewModelAssistedFactory<PlayerViewModel> {
        override fun create(
            handle: SavedStateHandle
        ): PlayerViewModel
    }

    private val _uiState = MutableStateFlow<UIState>(UIState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _track: MutableStateFlow<Container<TrackPlayer>> =
        MutableStateFlow(Container.Pending)
    val track: StateFlow<Container<TrackPlayer>> = _track.asStateFlow()

    private var listTracks = mutableListOf<TrackPlayer>()

    private val _duration = MutableStateFlow(savedStateHandle[DURATION_KEY] ?: 0L)
    val duration: StateFlow<Long> = _duration

    private val _progress = MutableStateFlow(savedStateHandle[PROGRESS_KEY] ?: 0F)
    val progress: StateFlow<Float> = _progress

    private val _progressString = MutableStateFlow(savedStateHandle[PROGRESS_STRING_KEY] ?: "00:00")
    val progressString: StateFlow<String> = _progressString

    private val _isPlaying = MutableStateFlow(savedStateHandle[IS_PLAYING_KEY] ?: false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _isRepeat = MutableStateFlow(false)
    val isRepeat: StateFlow<Boolean> = _isRepeat

    private val _isShuffle = MutableStateFlow(false)
    val isShuffle: StateFlow<Boolean> = _isShuffle

    init {
        viewModelScope.launch {
            mediaServiceHandler.mediaState.collect { mediaState ->
                when (mediaState) {
                    is PlayerMediaState.Buffering -> calculateProgressValues(mediaState.progress)
                    PlayerMediaState.Initial -> _uiState.value = UIState.Initial
                    is PlayerMediaState.Playing -> _isPlaying.value = mediaState.isPlaying
                    is PlayerMediaState.Progress -> {
                        if (mediaState.duration >= 0) _duration.value = mediaState.duration
                        _isPlaying.value = mediaState.isPlaying
                        _isRepeat.value = mediaState.isRepeat
                        calculateProgressValues(mediaState.progress)
                    }

                    is PlayerMediaState.Ready -> {
                        _duration.value = mediaState.duration
                        _isRepeat.value = mediaState.isRepeat
                        _uiState.value = UIState.Ready
                    }
                }
            }
        }
    }

    fun getTrack(trackId: Long) {
        viewModelScope.launch {
            getTrackPlayerUseCase.invoke(trackId = trackId).collect {
                _track.value = it
                if (it is Container.Success) {
                    loadTrackToHandler()
                }
            }
        }
    }

    fun repeatTrack(): Boolean = mediaServiceHandler.repeatTrack()

    fun shuffleTrack() {
        _isShuffle.value = !_isShuffle.value
    }

    fun onPlayerEvent(playerEvent: PlayerEvent) {
        viewModelScope.launch {
            mediaServiceHandler.onPlayerEvent(playerEvent)
        }
    }

    private suspend fun loadTrackToHandler() {
        track.value.suspendMap { track ->
            val mediaItem = MediaItem.Builder()
                .setMediaId(track.id.toString())
                .setUri(track.preview)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist(track.artist)
                        .setTitle(track.title)
                        .setArtworkUri(track.albumCover.toUri())
                        .build()
                ).build()

            mediaServiceHandler.addMediaItem(mediaItem)
        }
    }

    fun switchDownloadStatus(trackId: Long) {
        viewModelScope.launch {
            switchStatusTrackPlayerUseCase.invoke(trackId = trackId)
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatDuration(duration: Long): String {
        val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun calculateProgressValues(currentProgress: Long) {
        _progress.value =
            if (currentProgress > 0) (currentProgress.toFloat() / duration.value) else 0f
        _progressString.value = formatDuration(currentProgress)
    }

    override fun onCleared() {
        viewModelScope.launch {
            mediaServiceHandler.onPlayerEvent(PlayerEvent.Stop)
        }
    }

    companion object {
        private const val IS_PLAYING_KEY = "isPlaying"
        private const val IS_REPLAY_KEY = "isReplay"
        private const val PROGRESS_KEY = "progress"
        private const val PROGRESS_STRING_KEY = "progressString"
        private const val DURATION_KEY = "duration"
    }
}

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}