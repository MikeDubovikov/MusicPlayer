package com.mdubovikov.musicplayer.merging.tracks

import androidx.paging.PagingData
import androidx.paging.map
import com.mdubovikov.common.Container
import com.mdubovikov.data.TracksDataRepository
import com.mdubovikov.data.tracks.TracksMapper
import com.mdubovikov.tracks.domain.entities.Track
import com.mdubovikov.tracks.domain.repositories.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AdapterTracksRepository @Inject constructor(
    private val tracksDataRepository: TracksDataRepository,
    private val mapper: TracksMapper
) : TracksRepository {

    override fun getTracks(): Flow<Container<PagingData<Track>>> {
        return tracksDataRepository.getTracks().map { containerList ->
            containerList.suspendMap { list ->
                list.map { trackDto ->
                    mapper.toTracksDomain(trackDto)
                }
            }
        }
    }

    override fun searchTracks(query: String): Flow<Container<PagingData<Track>>> {
        return tracksDataRepository.searchTracks(query).map { containerList ->
            containerList.suspendMap { pagingData ->
                pagingData.map { trackDto ->
                    mapper.toTracksDomain(trackDto)
                }
            }
        }
    }

    override suspend fun addTrackToDownloads(trackId: Long) {
        tracksDataRepository.addTrackToDownloads(trackId = trackId)
    }

    override suspend fun removeTrackFromDownloads(trackId: Long) {
        tracksDataRepository.removeTrackFromDownloads(trackId = trackId)
    }

    override fun getTrackIdsInDownloads(): Flow<Set<Long>> {
        return tracksDataRepository.getTrackIdsInDownloads()
    }

}