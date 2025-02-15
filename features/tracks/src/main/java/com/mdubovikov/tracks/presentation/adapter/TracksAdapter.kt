package com.mdubovikov.tracks.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.mdubovikov.theme.databinding.TrackCardBinding
import com.mdubovikov.tracks.domain.entities.Track

class TracksAdapter(
    private val onTrackClick: ((trackId: Long) -> Unit)?
) : PagingDataAdapter<Track, TracksItemViewHolder>(TracksItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksItemViewHolder {

        val binding = TrackCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TracksItemViewHolder(binding, onTrackClick)
    }

    override fun onBindViewHolder(holder: TracksItemViewHolder, position: Int) {
        getItem(position)?.let { track ->
            holder.bind(track)
        }
    }
}