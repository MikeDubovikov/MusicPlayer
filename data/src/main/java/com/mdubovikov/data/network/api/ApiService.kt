package com.mdubovikov.data.network.api

import com.mdubovikov.data.network.dto.SearchResponse
import com.mdubovikov.data.network.dto.TrackDto
import com.mdubovikov.data.network.dto.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("chart")
    suspend fun getTracks(): TracksResponse

    @GET("track/{id}")
    suspend fun getTrack(@Path("id") trackId: Long): TrackDto

    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("index") index: Int = 0
    ): SearchResponse
}