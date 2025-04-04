package com.mdubovikov.downloads.di

import androidx.lifecycle.ViewModel
import com.mdubovikov.downloads.presentation.DownloadsViewModel
import com.mdubovikov.util.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DownloadsViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DownloadsViewModel::class)
    fun bindsDownloadsViewModel(
        viewModel: DownloadsViewModel
    ): ViewModel
}