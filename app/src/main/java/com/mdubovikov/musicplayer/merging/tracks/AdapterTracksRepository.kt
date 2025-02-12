package com.mdubovikov.musicplayer.merging.tracks

import com.mdubovikov.common.Container
import com.mdubovikov.data.TracksDataRepository
import com.mdubovikov.tracks.domain.entities.Track
import com.mdubovikov.tracks.domain.repositories.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AdapterTracksRepository @Inject constructor(
    private val tracksDataRepository: TracksDataRepository,
    private val mapper: TracksMapper
) : TracksRepository {

    override fun getTracks(): Flow<Container<List<Track>>> {
        return tracksDataRepository.getTracks().map {
            it.suspendMap { list ->
                list.map {
                    mapper.toTracksDomain(it)
                }
            }
        }
    }

    override fun searchTracks(query: String): Flow<Container<List<Track>>> {
        return tracksDataRepository.searchTracks(query = query).map {
            it.suspendMap { list ->
                list.map {
                    mapper.toTracksDomain(it)
                }
            }
        }
    }

}