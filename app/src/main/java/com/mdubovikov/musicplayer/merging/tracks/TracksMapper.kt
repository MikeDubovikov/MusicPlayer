package com.mdubovikov.musicplayer.merging.tracks

import com.mdubovikov.data.network.dto.TrackDto
import com.mdubovikov.tracks.domain.entities.Track
import javax.inject.Inject

class TracksMapper @Inject constructor() {

    fun toTracksDomain(dataEntity: TrackDto): Track {
        return Track(
            id = dataEntity.id,
            title = dataEntity.title,
            artist = dataEntity.artist.name,
            album = dataEntity.album.title,
            albumCover = dataEntity.album.cover,
            albumCoverBig = dataEntity.album.coverBig,
            duration = dataEntity.duration,
            preview = dataEntity.preview
        )
    }

}