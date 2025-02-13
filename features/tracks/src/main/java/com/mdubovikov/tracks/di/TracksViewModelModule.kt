package com.mdubovikov.tracks.di

import androidx.lifecycle.ViewModel
import com.mdubovikov.di.ViewModelKey
import com.mdubovikov.tracks.presentation.TracksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TracksViewModelModule {

    @IntoMap
    @ViewModelKey(TracksViewModel::class)
    @Binds
    fun bindViewModel(impl: TracksViewModel): ViewModel
}