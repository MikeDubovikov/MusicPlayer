package com.mdubovikov.tracks.domain

import com.mdubovikov.common.Container
import com.mdubovikov.tracks.domain.entities.Track
import com.mdubovikov.tracks.domain.repositories.TracksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {

    operator fun invoke(): Flow<Container<List<Track>>> {
        return tracksRepository.getTracks()
    }

}