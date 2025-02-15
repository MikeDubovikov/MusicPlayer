package com.mdubovikov.musicplayer.merging.downloads

import com.mdubovikov.data.DownloadsDataRepository
import com.mdubovikov.data.downloads.TracksDownloadsMapper
import com.mdubovikov.downloads.domain.entities.TrackDownloads
import com.mdubovikov.downloads.domain.repositories.DownloadsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AdapterDownloadsRepository @Inject constructor(
    private val downloadsDataRepository: DownloadsDataRepository,
    private val mapperDownloads: TracksDownloadsMapper
) : DownloadsRepository {

    override fun getDownloads(): Flow<List<TrackDownloads>> {
        return downloadsDataRepository.getDownloads().map { list ->
            list.map { trackDb ->
                mapperDownloads.toTracksDomain(trackDb)
            }
        }
    }

    override fun searchDownloads(query: String): Flow<List<TrackDownloads>> {
        return return downloadsDataRepository.searchDownloads(query = query).map { list ->
            list.map { trackDb ->
                mapperDownloads.toTracksDomain(trackDb)
            }
        }
    }
}