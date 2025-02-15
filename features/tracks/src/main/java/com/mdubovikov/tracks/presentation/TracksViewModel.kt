package com.mdubovikov.tracks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        getChart()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<Container<PagingData<Track>>> = _searchQuery
        .filter { query ->
            query.isNotEmpty()
        }
        .flatMapLatest { query ->
            searchTracksUseCase.invoke(query)
                .flatMapLatest { container ->
                    when (container) {
                        Container.Pending -> flowOf(Container.Pending)

                        is Container.Error -> flowOf(Container.Error(container.exception))

                        is Container.Success -> {
                            flowOf(container.value)
                                .cachedIn(viewModelScope)
                                .map { pagingData ->
                                    Container.Success(pagingData)
                                }
                        }
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Container.Pending
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getChart() {
        viewModelScope.launch {
            getTracksUseCase.invoke()
                .flatMapLatest { container ->
                    when (container) {
                        Container.Pending -> flowOf(Container.Pending)

                        is Container.Error -> flowOf(Container.Error(container.exception))

                        is Container.Success -> {
                            flowOf(container.value)
                                .cachedIn(viewModelScope)
                                .map { pagingData ->
                                    Container.Success(pagingData)
                                }
                        }
                    }
                }
                .collect {
                    _chartTracks.value = it
                }
        }
    }

    fun searchTracks(query: String) {
        _searchQuery.value = query
    }
}