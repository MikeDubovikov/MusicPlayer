package com.mdubovikov.tracks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.mdubovikov.common.Container
import com.mdubovikov.tracks.domain.GetTracksUseCase
import com.mdubovikov.tracks.domain.SearchTracksUseCase
import com.mdubovikov.tracks.domain.entities.Track
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class TracksViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase,
    private val searchTracksUseCase: SearchTracksUseCase
) : ViewModel() {

    private val _chartTracks: MutableStateFlow<Container<PagingData<Track>>> =
        MutableStateFlow<Container<PagingData<Track>>>(Container.Pending)
    val chartTracks: StateFlow<Container<PagingData<Track>>> = _chartTracks.asStateFlow()

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<Container<PagingData<Track>>> = _searchQuery
        .filter { query ->
            query.isNotEmpty()
        }
        .flatMapLatest { query ->
            searchTracksUseCase.invoke(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Container.Pending
        )

    init {
        getChart()
    }

    fun getChart() {
        viewModelScope.launch {
            getTracksUseCase.invoke().collect {
                _chartTracks.value = it
            }
        }
    }

    fun searchTracks(query: String) {
        _searchQuery.value = query
    }
}