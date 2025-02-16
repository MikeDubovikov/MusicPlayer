package com.mdubovikov.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class TrackDb(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val albumCover: String,
    val remoteUri: String,
    val localUri: String = ""
)