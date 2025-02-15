package com.mdubovikov.tracks.presentation

import android.content.Context
import android.os.Bundle
import android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.mdubovikov.common.Container
import com.mdubovikov.presentation.ViewModelFactory
import com.mdubovikov.presentation.observeStateOn
import com.mdubovikov.theme.R
import com.mdubovikov.tracks.TracksRouter
import com.mdubovikov.tracks.databinding.FragmentTracksBinding
import com.mdubovikov.tracks.di.TracksComponent
import com.mdubovikov.tracks.di.TracksComponentProvider
import com.mdubovikov.tracks.presentation.adapter.TracksAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class TracksFragment : Fragment(), TracksRouter {

    private var _binding: FragmentTracksBinding? = null
    private val binding: FragmentTracksBinding
        get() = _binding ?: throw IllegalStateException("Fragment $this binding cannot be accessed")

    lateinit var tracksComponent: TracksComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TracksViewModel::class.java]
    }

    private val tracksAdapter by lazy { TracksAdapter(::launchPlayer) }

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
        with(binding) {
            rvTracks.adapter = tracksAdapter
            rvTracks.itemAnimator = null
            tracksAdapter.addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        tracksError.visibility = View.GONE
                        rvTracks.visibility = View.GONE
                    }

                    is LoadState.NotLoading -> {
                        progressBar.visibility = View.GONE
                        tracksError.visibility = View.GONE
                        rvTracks.visibility = View.VISIBLE
                    }

                    is LoadState.Error -> {
                        progressBar.visibility = View.GONE
                        rvTracks.visibility = View.GONE
                        tracksError.visibility = View.VISIBLE
                    }
                }
            }
        }

        setupSearchView()

        if (viewModel.searchQuery.value.isNotEmpty()) {
            searchTracks()
        } else {
            loadChartTracks()
        }

        binding.buttonChart.setOnClickListener {
            viewModel.getChart()
        }
    }

    private fun loadChartTracks() {
        viewModel.chartTracks.observeStateOn(viewLifecycleOwner) { track ->
            if (track is Container.Success) {
                viewLifecycleOwner.lifecycleScope.launch {
                    tracksAdapter.submitData(track.value)
                }
            }
        }
    }

    private fun searchTracks() {
        viewModel.searchResults.observeStateOn(viewLifecycleOwner) { track ->
            with(binding) {
                progressBar.visibility = if (track is Container.Pending) View.VISIBLE else View.GONE
                tracksError.visibility = if (track is Container.Error) View.VISIBLE else View.GONE
                rvTracks.visibility = if (track is Container.Success) View.VISIBLE else View.GONE
                if (track is Container.Success) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        tracksAdapter.submitData(track.value)
                    }
                }
            }
        }
    }

    private fun setupSearchView() {
        val searchIcon =
            binding.svMeals.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setImageResource(R.drawable.ic_search)

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

    override fun launchPlayer(trackId: Long) {
        val action =
            TracksFragmentDirections.actionTracksFragmentToPlayerFragment(trackId = trackId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}