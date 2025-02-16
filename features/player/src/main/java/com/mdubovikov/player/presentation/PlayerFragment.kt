package com.mdubovikov.player.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mdubovikov.common.Container
import com.mdubovikov.player.databinding.FragmentPlayerBinding
import com.mdubovikov.player.di.PlayerComponent
import com.mdubovikov.player.di.PlayerComponentProvider
import com.mdubovikov.player.domain.entities.TrackPlayer
import com.mdubovikov.player.service.MusicService
import com.mdubovikov.presentation.BaseFragment
import com.mdubovikov.presentation.ViewModelFactory
import com.mdubovikov.presentation.observeStateOn
import com.mdubovikov.theme.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {

    private lateinit var playerComponent: PlayerComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PlayerViewModel::class.java]
    }

    private val args: PlayerFragmentArgs by navArgs<PlayerFragmentArgs>()

    private var service: MusicService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            service = (binder as MusicService.MusicBinder).getService()
//            binder.setMusicList()
            lifecycleScope.launch {
                binder.isPlaying().collectLatest {
                }
            }

            lifecycleScope.launch {
                binder.maxDuration().collectLatest {
                }
            }
            lifecycleScope.launch {
                binder.currentDuration().collectLatest {
                }
            }

            lifecycleScope.launch {
                binder.isPlaying().collectLatest {
                }
            }
            lifecycleScope.launch {
                binder.getCurrentTrack().collectLatest {
                }
            }
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun createBinding(): FragmentPlayerBinding {
        return FragmentPlayerBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        playerComponent =
            (requireActivity().applicationContext as PlayerComponentProvider).getPlayerComponent()
        playerComponent.inject(this)
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

            buttonPlayPause.setOnClickListener {
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

//    override fun onStart() {
//        super.onStart()
//        val intent = Intent(requireActivity(), MusicService::class.java)
//        requireActivity().startService(intent)
//        requireActivity().bindService(intent, connection, BIND_AUTO_CREATE)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        val intent = Intent(requireActivity(), MusicService::class.java)
//        requireActivity().stopService(intent)
//        requireActivity().unbindService(connection)
//    }

    private fun switchStatus(trackId: Long) {
        viewModel.switchStatus(trackId = trackId)
    }

    private fun formatTime(ms: Long): String {
        val minutes = (ms / 1000) / 60
        val seconds = (ms / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}