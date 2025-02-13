package com.mdubovikov.downloads.domain.entities

data class TrackDownloads(
    val id: Long,
    val title: String,
    val artist: String,
    val albumCover: String
)