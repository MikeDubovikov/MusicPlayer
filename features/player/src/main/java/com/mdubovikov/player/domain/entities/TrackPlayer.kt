package com.mdubovikov.player.domain.entities

data class TrackPlayer(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumCover: String,
    val duration: Int,
    val preview: String
)