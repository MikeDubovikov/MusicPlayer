package com.mdubovikov.musicplayer

import android.app.Application
import com.mdubovikov.downloads.di.DownloadsComponent
import com.mdubovikov.downloads.di.DownloadsComponentProvider
import com.mdubovikov.musicplayer.di.ApplicationComponent
import com.mdubovikov.musicplayer.di.DaggerApplicationComponent
import com.mdubovikov.player.di.PlayerComponent
import com.mdubovikov.player.di.PlayerComponentProvider
import com.mdubovikov.tracks.di.TracksComponent
import com.mdubovikov.tracks.di.TracksComponentProvider

class MusicPlayer : Application(), TracksComponentProvider, DownloadsComponentProvider,
    PlayerComponentProvider {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }

    override fun getTracksComponent(): TracksComponent {
        return applicationComponent.tracksComponent().create()
    }

    override fun getDownloadsComponent(): DownloadsComponent {
        return applicationComponent.downloadsComponent().create()
    }

    override fun getPlayerComponent(): PlayerComponent {
        return applicationComponent.playerComponent().create()
    }
}