package com.mdubovikov.musicplayer.di

import com.mdubovikov.di.ApplicationScope
import com.mdubovikov.musicplayer.merging.player.AdapterPlayerRepository
import com.mdubovikov.player.domain.repositories.PlayerRepository
import dagger.Binds
import dagger.Module

@Module
interface PlayerRepositoryModule {

    @[ApplicationScope Binds]
    fun bindPlayerRepository(impl: AdapterPlayerRepository): PlayerRepository

}