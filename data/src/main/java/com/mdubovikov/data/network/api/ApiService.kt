package com.mdubovikov.data.network.api

import com.mdubovikov.data.network.dto.SearchResponse
import com.mdubovikov.data.network.dto.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("chart")
    suspend fun getTracks(): TracksResponse

    @GET("search")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("index") index: Int
    ): SearchResponse

}