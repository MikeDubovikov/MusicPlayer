package com.mdubovikov.data.player

import com.mdubovikov.data.database.entity.TrackDb
import com.mdubovikov.data.network.dto.TrackDto
import com.mdubovikov.player.domain.entities.TrackPlayer
import javax.inject.Inject

class TracksPlayerMapper @Inject constructor() {

    fun toTrackPlayerDomain(trackDto: TrackDto): TrackPlayer {
        return TrackPlayer(
            id = trackDto.id,
            title = trackDto.title,
            artist = trackDto.artist.name,
            album = trackDto.album.title,
            albumCover = trackDto.album.coverBig,
            duration = trackDto.duration,
            preview = trackDto.preview
        )
    }

    fun toTrackPlayerDb(trackDto: TrackDto): TrackDb {
        return TrackDb(
            id = trackDto.id,
            title = trackDto.title,
            artist = trackDto.artist.name,
            albumCover = trackDto.album.coverBig
        )
    }

}