package com.mdubovikov.musicplayer.di

import com.mdubovikov.downloads.domain.repositories.DownloadsRepository
import com.mdubovikov.musicplayer.merging.downloads.AdapterDownloadsRepository
import com.mdubovikov.util.ApplicationScope
import dagger.Binds
import dagger.Module

@Module
interface DownloadsRepositoryModule {

    @[ApplicationScope Binds]
    fun bindDownloadsRepository(impl: AdapterDownloadsRepository): DownloadsRepository
}