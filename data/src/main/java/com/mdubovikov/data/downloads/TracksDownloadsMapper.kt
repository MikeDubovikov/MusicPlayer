package com.mdubovikov.data.downloads

import com.mdubovikov.data.database.entity.TrackDb
import com.mdubovikov.downloads.domain.entities.TrackDownloads
import javax.inject.Inject

class TracksDownloadsMapper @Inject constructor() {

    fun toTracksDomain(trackDb: TrackDb): TrackDownloads {
        return TrackDownloads(
            id = trackDb.id,
            title = trackDb.title,
            artist = trackDb.artist,
            albumCover = trackDb.albumCover,
        )
    }

}