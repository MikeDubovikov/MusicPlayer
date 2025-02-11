package com.mdubovikov.tracks.domain.repositories

import com.mdubovikov.common.Container
import com.mdubovikov.tracks.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    fun getTracks(): Flow<Container<List<Track>>>

    fun searchTracks(query: String): Flow<Container<List<Track>>>

}