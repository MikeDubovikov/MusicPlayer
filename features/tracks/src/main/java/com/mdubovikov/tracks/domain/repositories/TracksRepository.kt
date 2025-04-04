package com.mdubovikov.tracks.domain.repositories

import androidx.paging.PagingData
import com.mdubovikov.tracks.domain.entities.Track
import com.mdubovikov.util.Container
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    fun getTracks(): Flow<Container<PagingData<Track>>>

    fun searchTracks(query: String): Flow<Container<PagingData<Track>>>
}