package com.mdubovikov.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TracksDto(
    @SerialName("data") val data: List<TrackDto>,
    @SerialName("total") val total: Int
)