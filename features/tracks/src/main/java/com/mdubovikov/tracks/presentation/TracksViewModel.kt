package com.mdubovikov.tracks.presentation

import androidx.lifecycle.ViewModel
import com.mdubovikov.tracks.domain.GetTracksUseCase
import javax.inject.Inject

class TracksViewModel @Inject constructor(
    getTracksUseCase: GetTracksUseCase
) : ViewModel() {
}