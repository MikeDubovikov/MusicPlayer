package com.mdubovikov.player.domain.repositories

import com.mdubovikov.common.Container
import com.mdubovikov.player.domain.entities.TrackPlayer
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun getTrack(trackId: Long): Flow<Container<TrackPlayer>>

    suspend fun addTrackToDownloads(trackId: Long)

    suspend fun removeTrackFromDownloads(trackId: Long)

    fun getTrackIdsInDownloads(): Flow<Set<Long>>
}