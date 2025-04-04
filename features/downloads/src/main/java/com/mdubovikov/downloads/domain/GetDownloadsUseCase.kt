package com.mdubovikov.downloads.domain

import com.mdubovikov.downloads.domain.entities.TrackDownloads
import com.mdubovikov.downloads.domain.repositories.DownloadsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDownloadsUseCase @Inject constructor(
    private val downloadsRepository: DownloadsRepository
) {
    operator fun invoke(): Flow<List<TrackDownloads>> = downloadsRepository.getDownloads()
}