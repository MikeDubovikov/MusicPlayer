package com.mdubovikov.downloads.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mdubovikov.downloads.domain.entities.TrackDownloads

class DownloadsAdapter(
    private val onTrackClick: ((track: TrackDownloads) -> Unit)?,
    private val switchStatus: ((trackId: Long) -> Unit)?
) : ListAdapter<TrackDownloads, TracksItemViewHolder>(TracksItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksItemViewHolder {

        val binding = TrackCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TracksItemViewHolder(binding, onTrackClick, switchStatus)
    }

    override fun onBindViewHolder(holder: TracksItemViewHolder, position: Int) {
        getItem(position)?.let { track ->
            holder.bind(track)
        }
    }
}