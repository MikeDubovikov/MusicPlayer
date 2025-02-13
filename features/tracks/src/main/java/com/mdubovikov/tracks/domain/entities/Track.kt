package com.mdubovikov.tracks.domain.entities

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumCover: String,
    val albumCoverBig: String,
    val duration: Int,
    val preview: String
)