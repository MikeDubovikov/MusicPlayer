package com.mdubovikov.data

import com.mdubovikov.common.Container
import com.mdubovikov.data.network.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface PlayerDataRepository {

    fun getTrack(trackId: Long): Flow<Container<TrackDto>>

    suspend fun addTrackToDownloads(trackId: Long)

    suspend fun removeTrackFromDownloads(trackId: Long)

    fun getTrackIdsInDownloads(): Flow<Set<Long>>

}