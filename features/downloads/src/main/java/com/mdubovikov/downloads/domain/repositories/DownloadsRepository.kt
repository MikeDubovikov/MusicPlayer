package com.mdubovikov.downloads.domain.repositories

import com.mdubovikov.common.Container
import com.mdubovikov.downloads.domain.entities.TrackDownloads
import kotlinx.coroutines.flow.Flow

interface DownloadsRepository {

    fun getTracksDownloads(): Flow<Container<List<TrackDownloads>>>

    fun searchTracksDownloads(query: String): Flow<Container<List<TrackDownloads>>>

    fun switchStatusTrackDownloads(trackId: Long): Boolean

}