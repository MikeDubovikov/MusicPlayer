package com.mdubovikov.tracks.presentation

import android.content.Context
import android.os.Bundle
import android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.mdubovikov.common.Container
import com.mdubovikov.presentation.ViewModelFactory
import com.mdubovikov.tracks.databinding.FragmentTracksBinding
import com.mdubovikov.tracks.di.TracksComponent
import com.mdubovikov.tracks.di.TracksComponentProvider
import com.mdubovikov.tracks.domain.entities.Track
import com.mdubovikov.tracks.presentation.adapter.TracksAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TracksFragment : Fragment() {

    private var _binding: FragmentTracksBinding? = null
    private val binding: FragmentTracksBinding
        get() = _binding ?: throw IllegalStateException("Fragment $this binding cannot be accessed")

    lateinit var tracksComponent: TracksComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TracksViewModel::class.java]
    }

    private val tracksAdapter by lazy { TracksAdapter(::onTrackClick, ::switchStatus) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        tracksComponent =
            (requireActivity().applicationContext as TracksComponentProvider).getTracksComponent()
        tracksComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTracks.adapter = tracksAdapter
        tracksAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tracksError.visibility = View.GONE
                    binding.rvTracks.visibility = View.GONE
                }

                is LoadState.NotLoading -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tracksError.visibility = View.GONE
                    binding.rvTracks.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvTracks.visibility = View.GONE
                    binding.tracksError.visibility = View.VISIBLE
                }
            }
        }

        setupSearchView()
        loadChartTracks()

        binding.buttonChart.setOnClickListener {
            viewModel.getChart()
        }
    }

    private fun loadChartTracks() {

        lifecycleScope.launch {
            viewModel.chartTracks.collectLatest { track ->
                when (track) {
                    is Container.Pending -> {
                    }

                    is Container.Success -> {
                        tracksAdapter.submitData(track.value)
                    }

                    is Container.Error -> {
                    }
                }
            }
        }

    }

    private fun setupSearchView() {

        binding.svMeals.inputType = TYPE_TEXT_FLAG_CAP_SENTENCES

        binding.svMeals.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchTracks(query = query)
                    searchTracks()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun searchTracks() {

        lifecycleScope.launch {
            viewModel.searchResults.collectLatest { track ->
                when (track) {
                    is Container.Pending -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Container.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tracksError.visibility = View.GONE
                        binding.rvTracks.visibility = View.VISIBLE
                        tracksAdapter.submitData(track.value)
                    }

                    is Container.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvTracks.visibility = View.GONE
                        binding.tracksError.visibility = View.VISIBLE
                    }
                }
            }
        }

    }

    private fun onTrackClick(track: Track) {
    }

    private fun switchStatus(trackId: Long) {
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}