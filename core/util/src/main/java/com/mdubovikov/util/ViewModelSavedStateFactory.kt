package com.mdubovikov.util

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelSavedStateFactory @Inject constructor(
    private val viewModelAssistedFactory:
    @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModelAssistedFactory<out ViewModel>>>
) : AbstractSavedStateViewModelFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val factoryProvider = viewModelAssistedFactory[modelClass]
            ?: throw IllegalArgumentException("Unknown model class $modelClass")
        return factoryProvider.get().create(handle) as T
    }
}