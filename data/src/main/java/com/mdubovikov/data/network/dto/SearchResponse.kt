package com.mdubovikov.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("tracks") val tracks: List<TrackDto>,
    @SerialName("total") val total: Int,
    @SerialName("prev") val prev: String,
    @SerialName("next") val next: String
)