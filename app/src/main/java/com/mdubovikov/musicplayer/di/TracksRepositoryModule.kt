package com.mdubovikov.musicplayer.di

import com.mdubovikov.musicplayer.merging.tracks.AdapterTracksRepository
import com.mdubovikov.tracks.domain.repositories.TracksRepository
import com.mdubovikov.util.ApplicationScope
import dagger.Binds
import dagger.Module

@Module
interface TracksRepositoryModule {

    @[ApplicationScope Binds]
    fun bindTrackRepository(impl: AdapterTracksRepository): TracksRepository
}