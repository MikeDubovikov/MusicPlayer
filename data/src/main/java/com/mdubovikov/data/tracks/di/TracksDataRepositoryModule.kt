package com.mdubovikov.data.tracks.di

import com.mdubovikov.data.TracksDataRepository
import com.mdubovikov.data.network.api.ApiFactory
import com.mdubovikov.data.tracks.TrackDataRepositoryImpl
import com.mdubovikov.util.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface TracksDataRepositoryModule {

    @[ApplicationScope Binds]
    fun bindTrackDataRepository(impl: TrackDataRepositoryImpl): TracksDataRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService() = ApiFactory.apiService
    }
}