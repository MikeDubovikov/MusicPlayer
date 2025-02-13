package com.mdubovikov.data

import androidx.paging.PagingData
import com.mdubovikov.common.Container
import com.mdubovikov.data.network.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface TracksDataRepository {

    fun getTracks(): Flow<Container<PagingData<TrackDto>>>

    fun searchTracks(query: String): Flow<Container<PagingData<TrackDto>>>

}