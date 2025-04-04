package com.mdubovikov.tracks.presentation.adapter

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mdubovikov.theme.R
import com.mdubovikov.theme.databinding.TrackCardBinding
import com.mdubovikov.tracks.domain.entities.Track

class TracksItemViewHolder(
    private val binding: TrackCardBinding,
    private val onTrackClick: ((trackId: Long) -> Unit)?

) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        with(binding) {
            Glide.with(ivCover)
                .load(track.albumCover.toUri())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_music_note)
                .error(R.drawable.ic_music_note)
                .into(ivCover)

            tvTrackName.text = track.title
            tvArtistName.text = track.artist

            root.setOnClickListener { onTrackClick?.let { item -> item(track.id) } }
        }
    }
}