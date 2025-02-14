package com.mdubovikov.tracks.domain

import com.mdubovikov.tracks.domain.repositories.TracksRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SwitchStatusTrackUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {

    suspend operator fun invoke(trackId: Long): Boolean {
        return tracksRepository.getTrackIdsInDownloads().map {
            return@map if (!it.contains(trackId)) {
                tracksRepository.addTrackToDownloads(trackId)
                true
            } else {
                tracksRepository.removeTrackFromDownloads(trackId)
                false
            }
        }.first()
    }
}