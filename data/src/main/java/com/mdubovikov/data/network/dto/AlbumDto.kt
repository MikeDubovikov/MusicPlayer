package com.mdubovikov.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("cover_medium") val cover: String
)