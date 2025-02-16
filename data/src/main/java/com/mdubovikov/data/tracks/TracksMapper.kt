package com.mdubovikov.data.tracks

import com.mdubovikov.data.network.dto.TrackDto
import com.mdubovikov.tracks.domain.entities.Track
import javax.inject.Inject

class TracksMapper @Inject constructor() {

    fun toTracksDomain(trackDto: TrackDto): Track {
        return Track(
            id = trackDto.id,
            title = trackDto.title,
            artist = trackDto.artist.name,
            remoteUri = trackDto.remoteUri,
            albumCover = trackDto.album.cover,
        )
    }
}