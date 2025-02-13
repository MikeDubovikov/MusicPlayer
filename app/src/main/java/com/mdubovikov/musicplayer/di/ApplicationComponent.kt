package com.mdubovikov.musicplayer.di

import android.app.Application
import com.mdubovikov.data.tracks.di.TracksDataRepositoryModule
import com.mdubovikov.di.ApplicationScope
import com.mdubovikov.tracks.di.TracksComponent
import dagger.BindsInstance
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

    fun tracksComponent(): TracksComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}