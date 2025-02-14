package com.mdubovikov.downloads.domain

import com.mdubovikov.downloads.domain.repositories.DownloadsRepository
import javax.inject.Inject

class RemoveFromDownloadsUseCase @Inject constructor(
    private val downloadsRepository: DownloadsRepository
) {

    suspend operator fun invoke(trackId: Long) {
        downloadsRepository.removeTrackFromDownloads(trackId = trackId)
    }

}