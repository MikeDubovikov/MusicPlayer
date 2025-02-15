package com.mdubovikov.data.player.di

import com.mdubovikov.data.PlayerDataRepository
import com.mdubovikov.data.player.PlayerDataRepositoryImpl
import com.mdubovikov.di.ApplicationScope
import dagger.Binds
import dagger.Module

@Module
interface PlayerDataRepositoryModule {

    @[ApplicationScope Binds]
    fun bindPlayerDataRepository(impl: PlayerDataRepositoryImpl): PlayerDataRepository
}