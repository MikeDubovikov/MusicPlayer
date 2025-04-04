package com.mdubovikov.data.player

import com.mdubovikov.data.PlayerDataRepository
import com.mdubovikov.data.database.TrackDownloadManager
import com.mdubovikov.data.database.dao.TracksDao
import com.mdubovikov.data.network.api.ApiFactory
import com.mdubovikov.data.network.api.ApiService
import com.mdubovikov.data.network.dto.TrackDto
import com.mdubovikov.util.Container
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val downloadsDao: TracksDao,
    private val playerMapper: TracksPlayerMapper,
    private val trackDownloadManager: TrackDownloadManager
) : PlayerDataRepository {

    override fun getTrack(trackId: Long): Flow<Container<TrackDto>> {
        return flow {
            emit(Container.Pending)
            try {
                val track = apiService.getTrack(trackId)
                emit(Container.Success(track))
            } catch (e: Exception) {
                emit(Container.Error(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun addTrackToDownloads(trackId: Long) {
        val trackDto = ApiFactory.apiService.getTrack(trackId)
        downloadsDao.addToDownloads(playerMapper.toTrackPlayerDb(trackDto))
        trackDownloadManager.download(trackDto)
    }

    override suspend fun removeTrackFromDownloads(trackId: Long) {
        downloadsDao.removeFromDownloads(trackId = trackId)
        trackDownloadManager.delete(trackId)
    }

    override fun getTrackIdsInDownloads(): Flow<Set<Long>> {
        return downloadsDao.trackIdsFromDownloads().map { it.toSet() }
    }
}