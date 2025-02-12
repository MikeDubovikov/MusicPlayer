package com.mdubovikov.musicplayer.di

import com.mdubovikov.data.tracks.di.TracksDataRepositoryModule
import com.mdubovikov.di.ApplicationScope
import com.mdubovikov.musicplayer.MainActivity
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        TracksRepositoryModule::class,
        TracksDataRepositoryModule::class,
        TracksRouterModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(): ApplicationComponent
    }
}