package com.mdubovikov.player.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mdubovikov.player.databinding.FragmentPlayerBinding
import com.mdubovikov.player.di.PlayerComponent
import com.mdubovikov.player.di.PlayerComponentProvider
import com.mdubovikov.player.domain.entities.TrackPlayer
import com.mdubovikov.player.service.PlayerEvent
import com.mdubovikov.theme.R
import com.mdubovikov.util.BaseFragment
import com.mdubovikov.util.Container
import com.mdubovikov.util.ViewModelSavedStateFactory
import com.mdubovikov.util.observeStateOn
import javax.inject.Inject

class PlayerFragment : BaseFragment<FragmentPlayerBinding>() {

    private lateinit var playerComponent: PlayerComponent

    @Inject
    lateinit var viewModelSavedStateFactory: ViewModelSavedStateFactory

    private val viewModel: PlayerViewModel by viewModels { viewModelSavedStateFactory }

    private val args: PlayerFragmentArgs by navArgs<PlayerFragmentArgs>()

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
                    loadTrackUi(track.value)
                }
            }
        }
    }

    private fun loadTrackUi(trackPlayer: TrackPlayer) {
        with(binding) {
            tvTrackArtist.text = trackPlayer.artist
            tvArtistNameTop.text = trackPlayer.artist
            tvTrackTitle.text = trackPlayer.title

            Glide.with(ivAlbumCover)
                .load(trackPlayer.albumCover.toUri())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .error(R.drawable.ic_music_note)
                .into(ivAlbumCover)

            buttonBack.setOnClickListener {
                findNavController().navigateUp()
            }

            checkDownloadedState(trackPlayer.isDownload)

            buttonSwitchStatus.setOnClickListener {
                viewModel.switchDownloadStatus(trackId = trackPlayer.id)
                if (!trackPlayer.isDownload) {
                    showToast(R.string.track_added)
                } else {
                    showToast(R.string.track_removed)
                }
            }

            buttonPlayPause.setOnClickListener {
                viewModel.onPlayerEvent(PlayerEvent.PlayPause)
            }

            buttonPrev.setOnClickListener {
            }

            buttonNext.setOnClickListener {
            }

            viewModel.isRepeat.observeStateOn(viewLifecycleOwner) { isRepeat ->
                checkRepeatState(isRepeat)
            }

            buttonRepeat.setOnClickListener {
                val isRepeat = viewModel.repeatTrack()
                checkRepeatState(isRepeat)
                if (isRepeat) {
                    showToast(R.string.repeat_is_enable)
                } else {
                    showToast(R.string.repeat_is_disable)
                }
            }

            viewModel.isShuffle.observeStateOn(viewLifecycleOwner) { isShuffle ->
                checkShuffleState(isShuffle)
            }

            buttonShuffle.setOnClickListener {
                viewModel.shuffleTrack()
                if (viewModel.isShuffle.value) {
                    showToast(R.string.shuffle_is_enable)
                } else {
                    showToast(R.string.shuffle_is_disable)
                }
            }

            buttonBackwardProgress.setOnClickListener {
                viewModel.onPlayerEvent(PlayerEvent.Backward(5000))
            }

            buttonForwardProgress.setOnClickListener {
                viewModel.onPlayerEvent(PlayerEvent.Forward(5000))
            }

            viewModel.isPlaying.observeStateOn(viewLifecycleOwner) { isPlaying ->
                checkButtonState(isPlaying = isPlaying)
            }

            setSeekBar()

            viewModel.duration.observeStateOn(viewLifecycleOwner) { duration ->
                tvDuration.text = viewModel.formatDuration(duration)
            }

            viewModel.progressString.observeStateOn(viewLifecycleOwner) { progress ->
                tvCurrentTime.text = progress
            }
        }
    }

    private fun checkDownloadedState(isDownloaded: Boolean) {
        if (isDownloaded) {
            binding.buttonSwitchStatus.setImageResource(R.drawable.ic_added)
        } else {
            binding.buttonSwitchStatus.setImageResource(R.drawable.ic_add)
        }
    }

    private fun checkButtonState(isPlaying: Boolean) {
        if (isPlaying) {
            binding.buttonPlayPause.setImageResource(R.drawable.ic_pause)
        } else {
            binding.buttonPlayPause.setImageResource(R.drawable.ic_play)
        }
    }

    private fun checkRepeatState(isRepeat: Boolean) {
        if (isRepeat) {
            binding.buttonRepeat.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.orange))
        } else {
            binding.buttonRepeat.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.black))
        }
    }

    private fun checkShuffleState(isShuffle: Boolean) {
        if (isShuffle) {
            binding.buttonShuffle.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.orange))
        } else {
            binding.buttonShuffle.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.black))
        }
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(requireActivity(), messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun setSeekBar() {
        viewModel.progress.observeStateOn(viewLifecycleOwner) { progress ->
            binding.seekBar.progress = (progress * 100).toInt()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val duration = viewModel.duration.value
                    val currentTime = (duration * progress) / 100
                    binding.tvCurrentTime.text = viewModel.formatDuration(currentTime.toLong())
                    viewModel.onPlayerEvent(PlayerEvent.UpdateProgress(currentTime))
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}