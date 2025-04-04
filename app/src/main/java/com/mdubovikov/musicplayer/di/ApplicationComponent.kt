package com.mdubovikov.musicplayer.di

import android.app.Application
import com.mdubovikov.data.downloads.di.DownloadsDataRepositoryModule
import com.mdubovikov.data.player.di.PlayerDataRepositoryModule
import com.mdubovikov.data.tracks.di.TracksDataRepositoryModule
import com.mdubovikov.downloads.di.DownloadsComponent
import com.mdubovikov.player.di.PlayerComponent
import com.mdubovikov.tracks.di.TracksComponent
import com.mdubovikov.util.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        TracksRepositoryModule::class,
        TracksDataRepositoryModule::class,
        DownloadsRepositoryModule::class,
        DownloadsDataRepositoryModule::class,
        PlayerRepositoryModule::class,
        PlayerDataRepositoryModule::class
    ]
)
interface ApplicationComponent {

    fun tracksComponent(): TracksComponent.Factory

    fun downloadsComponent(): DownloadsComponent.Factory

    fun playerComponent(): PlayerComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}