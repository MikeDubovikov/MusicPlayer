package com.mdubovikov.data.downloads.di

import android.app.Application
import com.mdubovikov.data.DownloadsDataRepository
import com.mdubovikov.data.database.TrackDownloadManager
import com.mdubovikov.data.database.TracksDatabase
import com.mdubovikov.data.database.dao.TracksDao
import com.mdubovikov.data.downloads.DownloadsDataRepositoryImpl
import com.mdubovikov.util.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DownloadsDataRepositoryModule {

    @[ApplicationScope Binds]
    fun bindDownloadsDataRepository(impl: DownloadsDataRepositoryImpl): DownloadsDataRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideDatabase(application: Application): TracksDatabase {
            return TracksDatabase.getInstance(context = application)
        }

        @[ApplicationScope Provides]
        fun provideTracksDao(database: TracksDatabase): TracksDao {
            return database.downloadsDao()
        }

        @[ApplicationScope Provides]
        fun provideDownloadManager(application: Application): TrackDownloadManager {
            return TrackDownloadManager(context = application)
        }
    }
}