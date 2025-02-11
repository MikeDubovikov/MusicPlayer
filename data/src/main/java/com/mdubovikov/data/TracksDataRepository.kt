package com.mdubovikov.data

import com.mdubovikov.common.Container
import com.mdubovikov.data.network.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface TracksDataRepository {

    fun getTracks(): Flow<Container<List<TrackDto>>>

    fun searchTracks(query: String, index: Int): Flow<Container<List<TrackDto>>>

}