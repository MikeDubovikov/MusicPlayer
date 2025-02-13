package com.mdubovikov.downloads.domain

import com.mdubovikov.downloads.domain.repositories.DownloadsRepository
import javax.inject.Inject

class SwitchStatusDownloadsUseCase @Inject constructor(
    private val downloadsRepository: DownloadsRepository
) {
}