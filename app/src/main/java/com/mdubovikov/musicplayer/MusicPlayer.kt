package com.mdubovikov.musicplayer

import android.app.Application
import com.mdubovikov.musicplayer.di.ApplicationComponent
import com.mdubovikov.musicplayer.di.DaggerApplicationComponent

class MusicPlayer : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create()
    }
}