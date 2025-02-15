package com.mdubovikov.downloads.domain.repositories

import com.mdubovikov.downloads.domain.entities.TrackDownloads
import kotlinx.coroutines.flow.Flow

interface DownloadsRepository {

    fun getDownloads(): Flow<List<TrackDownloads>>

    fun searchDownloads(query: String): Flow<List<TrackDownloads>>
}