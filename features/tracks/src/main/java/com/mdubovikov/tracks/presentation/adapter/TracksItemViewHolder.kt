package com.mdubovikov.tracks.presentation.adapter

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
                .load(Uri.parse(track.albumCover))
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(ivCover)

            tvTrackName.text = track.title
            tvArtistName.text = track.artist

            root.setOnClickListener { onTrackClick?.let { item -> item(track.id) } }
        }
    }
}