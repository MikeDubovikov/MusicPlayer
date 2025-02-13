package com.mdubovikov.player.di

import com.mdubovikov.player.presentation.PlayerFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        PlayerViewModelModule::class
    ]
)
interface PlayerComponent {

    fun inject(playerFragment: PlayerFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): PlayerComponent
    }
}