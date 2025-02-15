package com.mdubovikov.musicplayer.merging.player

import com.mdubovikov.common.Container
import com.mdubovikov.data.PlayerDataRepository
import com.mdubovikov.data.player.TracksPlayerMapper
import com.mdubovikov.player.domain.entities.TrackPlayer
import com.mdubovikov.player.domain.repositories.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AdapterPlayerRepository @Inject constructor(
    private val playerDataRepository: PlayerDataRepository,
    private val playerMapper: TracksPlayerMapper
) : PlayerRepository {

    override fun getTrack(trackId: Long): Flow<Container<TrackPlayer>> {
        return playerDataRepository.getTrack(trackId = trackId).map { containerList ->
            containerList.suspendMap { track ->
                playerMapper.toTrackPlayerDomain(track)
            }
        }
    }

    override suspend fun addTrackToDownloads(trackId: Long) {
        playerDataRepository.addTrackToDownloads(trackId = trackId)
    }

    override suspend fun removeTrackFromDownloads(trackId: Long) {
        playerDataRepository.removeTrackFromDownloads(trackId = trackId)
    }

    override fun getTrackIdsInDownloads(): Flow<Set<Long>> {
        return playerDataRepository.getTrackIdsInDownloads()
    }

}