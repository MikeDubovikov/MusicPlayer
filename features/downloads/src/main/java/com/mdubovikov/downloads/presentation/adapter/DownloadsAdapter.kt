package com.mdubovikov.downloads.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.mdubovikov.downloads.domain.entities.TrackDownloads
import com.mdubovikov.theme.databinding.TrackCardBinding

class DownloadsAdapter(
    private val onTrackClick: ((trackId: Long) -> Unit)?
) : ListAdapter<TrackDownloads, DownloadsItemViewHolder>(DownloadsItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsItemViewHolder {

        val binding = TrackCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DownloadsItemViewHolder(binding, onTrackClick)
    }

    override fun onBindViewHolder(holder: DownloadsItemViewHolder, position: Int) {
        getItem(position)?.let { track ->
            holder.bind(track)
        }
    }
}