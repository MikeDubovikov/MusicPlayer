package com.mdubovikov.data.player

import com.mdubovikov.data.network.dto.TrackDto
import com.mdubovikov.player.domain.entities.TrackPlayer
import javax.inject.Inject

class TracksPlayerMapper @Inject constructor() {

    fun toTrackPlayerDomain(dataEntity: TrackDto): TrackPlayer {
        return TrackPlayer(
            id = dataEntity.id,
            title = dataEntity.title,
            artist = dataEntity.artist.name,
            album = dataEntity.album.title,
            albumCover = dataEntity.album.coverBig,
            duration = dataEntity.duration,
            preview = dataEntity.preview
        )
    }

}