package com.mdubovikov.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("data") val tracks: List<TrackDto>,
    @SerialName("total") val total: Int,
    @SerialName("prev") val prev: String? = null,
    @SerialName("next") val next: String? = null
)