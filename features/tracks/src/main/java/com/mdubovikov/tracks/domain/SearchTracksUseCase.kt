package com.mdubovikov.tracks.domain

import androidx.paging.PagingData
import com.mdubovikov.common.Container
import com.mdubovikov.tracks.domain.entities.Track
import com.mdubovikov.tracks.domain.repositories.TracksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {
    operator fun invoke(query: String): Flow<Container<PagingData<Track>>> {
        return tracksRepository.searchTracks(query = query)
    }
}