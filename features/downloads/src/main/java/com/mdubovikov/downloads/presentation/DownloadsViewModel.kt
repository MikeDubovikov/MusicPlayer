package com.mdubovikov.downloads.presentation

import androidx.lifecycle.ViewModel
import com.mdubovikov.downloads.domain.GetDownloadsUseCase
import com.mdubovikov.downloads.domain.SearchDownloadsUseCase
import com.mdubovikov.downloads.domain.SwitchStatusDownloadsUseCase
import javax.inject.Inject

class DownloadsViewModel @Inject constructor(
    private val getDownloadsUseCase: GetDownloadsUseCase,
    private val searchDownloadsUseCase: SearchDownloadsUseCase,
    private val switchStatusDownloadsUseCase: SwitchStatusDownloadsUseCase
) : ViewModel()