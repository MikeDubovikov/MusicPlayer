package com.mdubovikov.downloads.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdubovikov.downloads.domain.GetDownloadsUseCase
import com.mdubovikov.downloads.domain.SearchDownloadsUseCase
import com.mdubovikov.downloads.domain.entities.TrackDownloads
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class DownloadsViewModel @Inject constructor(
    private val getDownloadsUseCase: GetDownloadsUseCase,
    private val searchDownloadsUseCase: SearchDownloadsUseCase
) : ViewModel() {

    private val _downloadedTracks: MutableSharedFlow<List<TrackDownloads>> =
        MutableSharedFlow<List<TrackDownloads>>(replay = 1)
    val downloadedTracks: SharedFlow<List<TrackDownloads>> =
        _downloadedTracks.asSharedFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults = _searchQuery
        .filter { query ->
            query.isNotEmpty()
        }
        .flatMapLatest { query ->
            searchDownloadsUseCase.invoke(query = query)
        }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed()
        )

    init {
        getDownloads()
    }

    fun getDownloads() {
        viewModelScope.launch {
            getDownloadsUseCase().collect { tracks ->
                _downloadedTracks.emit(tracks)
            }
        }
    }

    fun searchTracksFromDownloads(query: String) {
        _searchQuery.value = query
    }
}