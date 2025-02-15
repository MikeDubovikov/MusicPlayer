package com.mdubovikov.player.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mdubovikov.common.Container
import com.mdubovikov.player.databinding.FragmentPlayerBinding
import com.mdubovikov.player.di.PlayerComponent
import com.mdubovikov.player.di.PlayerComponentProvider
import com.mdubovikov.player.domain.entities.TrackPlayer
import com.mdubovikov.presentation.ViewModelFactory
import com.mdubovikov.presentation.observeStateOn
import com.mdubovikov.theme.R
import javax.inject.Inject

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding: FragmentPlayerBinding
        get() = _binding ?: throw IllegalStateException("Fragment $this binding cannot be accessed")

    lateinit var playerComponent: PlayerComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PlayerViewModel::class.java]
    }

    private val args: PlayerFragmentArgs by navArgs<PlayerFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerComponent =
            (requireActivity().applicationContext as PlayerComponentProvider).getPlayerComponent()
        playerComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTrack(trackId = args.trackId)
        loadTrack()
    }

    private fun loadTrack() {
        viewModel.track.observeStateOn(viewLifecycleOwner) { track ->
            with(binding) {
                trackError.visibility = if (track is Container.Error) View.VISIBLE else View.GONE
                if (track is Container.Success) {
                    loadTrackData(track.value)
                }
            }
        }
    }


    private fun loadTrackData(trackPlayer: TrackPlayer) {
        with(binding) {
            tvTrackArtist.text = trackPlayer.artist
            tvArtistNameTop.text = trackPlayer.artist
            tvTrackTitle.text = trackPlayer.title

            Glide.with(ivAlbumCover)
                .load(Uri.parse(trackPlayer.albumCover))
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(ivAlbumCover)

            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }

            buttonPrev.setOnClickListener {
            }

            buttonPlay.setOnClickListener {
            }

            buttonNext.setOnClickListener {
            }

            buttonSwitchStatus.setOnClickListener {
                switchStatus(trackPlayer.id)
            }

            if (trackPlayer.isDownload) {
                buttonSwitchStatus.setImageResource(R.drawable.ic_added)
            } else {
                buttonSwitchStatus.setImageResource(R.drawable.ic_add)
            }
        }
    }

    private fun switchStatus(trackId: Long) {
        viewModel.switchStatus(trackId = trackId)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}