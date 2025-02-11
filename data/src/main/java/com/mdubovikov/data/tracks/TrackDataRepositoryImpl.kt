package com.mdubovikov.data.tracks

import com.mdubovikov.common.Container
import com.mdubovikov.data.TracksDataRepository
import com.mdubovikov.data.network.api.ApiService
import com.mdubovikov.data.network.dto.TrackDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TrackDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TracksDataRepository {

    override fun getTracks(): Flow<Container<List<TrackDto>>> = flow {
        emit(Container.Pending)
        try {
            val response = apiService.getTracks()
            emit(Container.Success(response.tracks.data))
        } catch (e: Exception) {
            emit(Container.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun searchTracks(query: String, index: Int): Flow<Container<List<TrackDto>>> = flow {
        emit(Container.Pending)
        try {
            val response = apiService.searchTracks(query = query, index = index)
            emit(Container.Success(response.tracks))
        } catch (e: Exception) {
            emit(Container.Error(e))
        }
    }.flowOn(Dispatchers.IO)

}