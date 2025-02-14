package com.mdubovikov.data.tracks

import com.mdubovikov.data.database.entity.TrackDb
import com.mdubovikov.data.network.dto.TrackDto
import com.mdubovikov.tracks.domain.entities.Track
import javax.inject.Inject

class TracksMapper @Inject constructor() {

    fun toTracksDomain(dataEntity: TrackDto): Track {
        return Track(
            id = dataEntity.id,
            title = dataEntity.title,
            artist = dataEntity.artist.name,
            albumCover = dataEntity.album.cover,
        )
    }

    fun toTracksDb(dataEntity: TrackDto): TrackDb {
        return TrackDb(
            id = dataEntity.id,
            title = dataEntity.title,
            artist = dataEntity.artist.name,
            albumCover = dataEntity.album.cover,
        )
    }

}