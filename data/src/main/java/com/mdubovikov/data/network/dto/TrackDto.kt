package com.mdubovikov.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("artist") val artist: ArtistDto,
    @SerialName("album") val album: AlbumDto,
    @SerialName("duration") val duration: Int,
    @SerialName("preview") val remoteUri: String
)