package com.mdubovikov.downloads.di

import com.mdubovikov.downloads.presentation.DownloadsFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        DownloadsViewModelModule::class
    ]
)
interface DownloadsComponent {

    fun inject(downloadsFragment: DownloadsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): DownloadsComponent
    }
}