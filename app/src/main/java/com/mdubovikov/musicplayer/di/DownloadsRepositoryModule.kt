package com.mdubovikov.musicplayer.di

import com.mdubovikov.di.ApplicationScope
import com.mdubovikov.downloads.domain.repositories.DownloadsRepository
import com.mdubovikov.musicplayer.merging.downloads.AdapterDownloadsRepository
import dagger.Binds
import dagger.Module

@Module
interface DownloadsRepositoryModule {

    @[ApplicationScope Binds]
    fun bindDownloadsRepository(impl: AdapterDownloadsRepository): DownloadsRepository

}