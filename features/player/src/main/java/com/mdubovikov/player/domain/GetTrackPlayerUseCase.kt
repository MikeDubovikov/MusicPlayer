package com.mdubovikov.player.domain

import com.mdubovikov.common.Container
import com.mdubovikov.player.domain.entities.TrackPlayer
import com.mdubovikov.player.domain.repositories.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTrackPlayerUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {

    operator fun invoke(trackId: Long): Flow<Container<TrackPlayer>> {
        return combine(
            playerRepository.getTrack(trackId = trackId),
            playerRepository.getTrackIdsInDownloads()
        ) { trackContainer, idsInDownloads ->
            if (trackContainer !is Container.Success) return@combine trackContainer.map()
            val track = trackContainer.value
            val trackResult = track.copy(isDownload = idsInDownloads.contains(track.id))
            return@combine Container.Success(trackResult)
        }
    }

}