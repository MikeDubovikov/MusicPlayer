package com.mdubovikov.musicplayer.di

import com.mdubovikov.di.ApplicationScope
import com.mdubovikov.musicplayer.merging.tracks.AdapterTracksRouter
import com.mdubovikov.tracks.TracksRouter
import dagger.Binds
import dagger.Module

@Module
interface TracksRouterModule {

    @[ApplicationScope Binds]
    fun bindTrackRouter(impl: AdapterTracksRouter): TracksRouter

}