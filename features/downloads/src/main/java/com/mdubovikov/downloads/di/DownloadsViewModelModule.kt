package com.mdubovikov.downloads.di

import androidx.lifecycle.ViewModel
import com.mdubovikov.di.ViewModelKey
import com.mdubovikov.downloads.presentation.DownloadsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DownloadsViewModelModule {

    @IntoMap
    @ViewModelKey(DownloadsViewModel::class)
    @Binds
    fun bindViewModel(impl: DownloadsViewModel): ViewModel
}