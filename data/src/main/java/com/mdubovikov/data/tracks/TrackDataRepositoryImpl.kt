package com.mdubovikov.data.tracks

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mdubovikov.data.TracksDataRepository
import com.mdubovikov.data.network.api.ApiService
import com.mdubovikov.data.network.dto.TrackDto
import com.mdubovikov.data.network.paging.TracksPageSource
import com.mdubovikov.util.Container
import com.mdubovikov.util.ResponseOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class TrackDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TracksDataRepository {

    override fun getTracks(): Flow<Container<PagingData<TrackDto>>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        ),
        pagingSourceFactory = { TracksPageSource(apiService, ResponseOption.CHART_TRACKS) }
    ).flow
        .map<PagingData<TrackDto>, Container<PagingData<TrackDto>>> { pagingData ->
            Container.Success(pagingData)
        }
        .onStart {
            emit(Container.Pending)
        }
        .catch { e ->
            emit(Container.Error(e as? Exception ?: RuntimeException(e)))
        }

    override fun searchTracks(query: String): Flow<Container<PagingData<TrackDto>>> = Pager(
        config = PagingConfig(
            pageSize = 25,
            enablePlaceholders = false,
            initialLoadSize = 25
        ),
        pagingSourceFactory = { TracksPageSource(apiService, ResponseOption.SEARCH_TRACKS, query) }
    ).flow
        .map<PagingData<TrackDto>, Container<PagingData<TrackDto>>> { pagingData ->
            Container.Success(pagingData)
        }
        .onStart {
            emit(Container.Pending)
        }
        .catch { e ->
            emit(Container.Error(e as? Exception ?: RuntimeException(e)))
        }
}