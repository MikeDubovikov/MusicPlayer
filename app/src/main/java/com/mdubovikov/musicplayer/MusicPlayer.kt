package com.mdubovikov.musicplayer

import android.app.Application
import com.mdubovikov.musicplayer.di.ApplicationComponent
import com.mdubovikov.musicplayer.di.DaggerApplicationComponent
import com.mdubovikov.tracks.di.TracksComponent
import com.mdubovikov.tracks.di.TracksComponentProvider

class MusicPlayer : Application(), TracksComponentProvider {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }

    override fun getTracksComponent(): TracksComponent {
        return applicationComponent.tracksComponent().create()
    }
}