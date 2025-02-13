package com.mdubovikov.player.di

import androidx.lifecycle.ViewModel
import com.mdubovikov.di.ViewModelKey
import com.mdubovikov.player.presentation.PlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PlayerViewModelModule {

    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    @Binds
    fun bindViewModel(impl: PlayerViewModel): ViewModel
}