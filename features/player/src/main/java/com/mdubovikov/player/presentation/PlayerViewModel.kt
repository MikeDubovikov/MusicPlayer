package com.mdubovikov.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdubovikov.common.Container
import com.mdubovikov.player.domain.GetTrackPlayerUseCase
import com.mdubovikov.player.domain.SwitchStatusTrackPlayerUseCase
import com.mdubovikov.player.domain.entities.TrackPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    private val getTrackPlayerUseCase: GetTrackPlayerUseCase,
    private val switchStatusTrackPlayerUseCase: SwitchStatusTrackPlayerUseCase
) : ViewModel() {

    private val _track: MutableStateFlow<Container<TrackPlayer>> =
        MutableStateFlow(Container.Pending)
    val track: StateFlow<Container<TrackPlayer>> = _track.asStateFlow()

    private val _currentTrack = MutableStateFlow<TrackPlayer?>(null)
    val currentTrack: StateFlow<TrackPlayer?> = _currentTrack

    private val _maxDuration = MutableStateFlow(0f)
    val maxDuration: StateFlow<Float> = _maxDuration

    private val _currentDuration = MutableStateFlow(0f)
    val currentDuration: StateFlow<Float> = _currentDuration

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun getTrack(trackId: Long) {
        viewModelScope.launch {
            getTrackPlayerUseCase.invoke(trackId = trackId).collect {
                _track.value = it
            }
        }
    }

    fun switchStatus(trackId: Long) {
        viewModelScope.launch {
            switchStatusTrackPlayerUseCase.invoke(trackId = trackId)
        }
    }
}