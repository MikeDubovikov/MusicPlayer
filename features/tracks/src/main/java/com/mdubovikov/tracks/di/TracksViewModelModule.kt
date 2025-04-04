package com.mdubovikov.tracks.di

import androidx.lifecycle.ViewModel
import com.mdubovikov.tracks.presentation.TracksViewModel
import com.mdubovikov.util.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TracksViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TracksViewModel::class)
    fun bindsTracksViewModel(
        viewModel: TracksViewModel
    ): ViewModel
}