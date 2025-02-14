package com.mdubovikov.tracks.domain.entities

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val albumCover: String,
    val isDownload: Boolean = false
)