package com.mdubovikov.tracks.di

import com.mdubovikov.tracks.presentation.TracksFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        TracksViewModelModule::class
    ]
)
interface TracksComponent {

    fun inject(tracksFragment: TracksFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): TracksComponent
    }
}