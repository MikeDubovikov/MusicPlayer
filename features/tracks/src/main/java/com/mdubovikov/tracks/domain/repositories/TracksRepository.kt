package com.mdubovikov.tracks.domain.repositories

import androidx.paging.PagingData
import com.mdubovikov.common.Container
import com.mdubovikov.tracks.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    fun getTracks(): Flow<Container<PagingData<Track>>>

    fun searchTracks(query: String): Flow<Container<PagingData<Track>>>

    suspend fun addTrackToDownloads(trackId: Long)

    suspend fun removeTrackFromDownloads(trackId: Long)

    fun getTrackIdsInDownloads(): Flow<Set<Long>>

}