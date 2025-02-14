package com.mdubovikov.downloads.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.mdubovikov.downloads.domain.entities.TrackDownloads

class DownloadsItemDiffCallback : DiffUtil.ItemCallback<TrackDownloads>() {

    override fun areItemsTheSame(oldItem: TrackDownloads, newItem: TrackDownloads): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrackDownloads, newItem: TrackDownloads): Boolean {
        return oldItem == newItem
    }
}