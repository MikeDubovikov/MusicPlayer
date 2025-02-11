package com.mdubovikov.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("picture") val picture: String
)