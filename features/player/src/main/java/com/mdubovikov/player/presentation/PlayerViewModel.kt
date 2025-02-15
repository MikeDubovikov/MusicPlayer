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
        MutableStateFlow<Container<TrackPlayer>>(Container.Pending)
    val track: StateFlow<Container<TrackPlayer>> = _track.asStateFlow()

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