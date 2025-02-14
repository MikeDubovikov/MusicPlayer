package com.mdubovikov.tracks.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.mdubovikov.common.Container
import com.mdubovikov.tracks.domain.entities.Track
import com.mdubovikov.tracks.domain.repositories.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {

    operator fun invoke(): Flow<Container<PagingData<Track>>> {
        return combine(
            tracksRepository.getTracks(),
            tracksRepository.getTrackIdsInDownloads()
        ) { tracksContainer, idsInDownloads ->
            if (tracksContainer !is Container.Success) return@combine tracksContainer.map()
            val tracks = tracksContainer.value
            val tracksResult = tracks.map { track ->
                track.copy(isDownload = idsInDownloads.contains(track.id))
            }
            return@combine Container.Success(tracksResult)
        }
    }

}