package com.mdubovikov.downloads.domain

import com.mdubovikov.downloads.domain.entities.TrackDownloads
import com.mdubovikov.downloads.domain.repositories.DownloadsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SearchDownloadsUseCase @Inject constructor(
    private val downloadsRepository: DownloadsRepository
) {

    operator fun invoke(query: String): Flow<List<TrackDownloads>> {
        return combine(
            downloadsRepository.searchDownloads(query = query),
            downloadsRepository.getTrackIdsInDownloads()
        ) { tracks, idsInDownloads ->
            return@combine tracks.map { trackDownloaded ->
                trackDownloaded.copy(isDownload = idsInDownloads.contains(trackDownloaded.id))
            }
        }
    }

}