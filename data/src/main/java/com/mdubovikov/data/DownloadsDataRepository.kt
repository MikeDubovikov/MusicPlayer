package com.mdubovikov.data

import com.mdubovikov.data.database.entity.TrackDb
import kotlinx.coroutines.flow.Flow

interface DownloadsDataRepository {

    fun getDownloads(): Flow<List<TrackDb>>

    fun searchDownloads(query: String): Flow<List<TrackDb>>
}