package com.mdubovikov.tracks.domain

import androidx.paging.PagingData
import com.mdubovikov.tracks.domain.entities.Track
import com.mdubovikov.tracks.domain.repositories.TracksRepository
import com.mdubovikov.util.Container
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {
    operator fun invoke(): Flow<Container<PagingData<Track>>> {
        return tracksRepository.getTracks()
    }
}