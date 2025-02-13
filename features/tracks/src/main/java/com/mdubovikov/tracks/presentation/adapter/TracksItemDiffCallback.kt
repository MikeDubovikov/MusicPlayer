package com.mdubovikov.tracks.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mdubovikov.tracks.domain.entities.Track

class TracksItemDiffCallback : DiffUtil.ItemCallback<Track>() {

    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}