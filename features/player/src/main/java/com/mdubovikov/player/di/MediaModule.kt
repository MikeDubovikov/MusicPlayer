package com.mdubovikov.player.di

import android.app.Application
import com.mdubovikov.player.service.MediaServiceHandler
import com.mdubovikov.util.PlayerScope
import dagger.Module
import dagger.Provides

@Module
class MediaModule {

    @Provides
    @PlayerScope
    fun provideServiceHandler(application: Application): MediaServiceHandler {
        return MediaServiceHandler.getInstance(application = application)
    }
}