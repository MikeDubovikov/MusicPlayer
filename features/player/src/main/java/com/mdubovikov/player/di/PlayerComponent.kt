package com.mdubovikov.player.di

import com.mdubovikov.player.presentation.PlayerFragment
import com.mdubovikov.util.PlayerScope
import dagger.Subcomponent

@PlayerScope
@Subcomponent(
    modules = [
        PlayerViewModelModule::class,
        MediaModule::class
    ]
)
interface PlayerComponent {

    fun inject(playerFragment: PlayerFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): PlayerComponent
    }
}