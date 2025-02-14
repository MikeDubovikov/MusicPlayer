package com.mdubovikov.data.downloads

import com.mdubovikov.data.DownloadsDataRepository
import com.mdubovikov.data.database.dao.TracksDao
import com.mdubovikov.data.database.entity.TrackDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DownloadsDataRepositoryImpl @Inject constructor(
    private val downloadsDao: TracksDao
) : DownloadsDataRepository {

    override fun getDownloads(): Flow<List<TrackDb>> {
        return downloadsDao.getDownloads()
    }

    override fun searchDownloads(query: String): Flow<List<TrackDb>> {
        return downloadsDao.searchTracksFromDownloads(query = query)
    }

    override suspend fun removeTrackFromDownloads(trackId: Long) {
        downloadsDao.removeFromDownloads(trackId = trackId)
    }

    override fun getTrackIdsInDownloads(): Flow<Set<Long>> {
        return downloadsDao.trackIdsFromDownloads().map { it.toSet() }
    }

}