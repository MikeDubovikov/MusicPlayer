package com.mdubovikov.player.di

import androidx.lifecycle.ViewModel
import com.mdubovikov.player.presentation.PlayerViewModel
import com.mdubovikov.util.ViewModelAssistedFactory
import com.mdubovikov.util.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PlayerViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    fun bindsPlayerViewModelFactory(
        factory: PlayerViewModel.Factory
    ): ViewModelAssistedFactory<out ViewModel>
}