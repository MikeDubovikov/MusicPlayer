package com.mdubovikov.downloads.presentation

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
import androidx.navigation.fragment.findNavController
import com.mdubovikov.downloads.DownloadsRouter
import com.mdubovikov.downloads.databinding.FragmentDownloadsBinding
import com.mdubovikov.downloads.di.DownloadsComponent
import com.mdubovikov.downloads.di.DownloadsComponentProvider
import com.mdubovikov.downloads.presentation.adapter.DownloadsAdapter
import com.mdubovikov.presentation.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DownloadsFragment : Fragment(), DownloadsRouter {

    private var _binding: FragmentDownloadsBinding? = null
    private val binding: FragmentDownloadsBinding
        get() = _binding ?: throw IllegalStateException("Fragment $this binding cannot be accessed")

    lateinit var downloadsComponent: DownloadsComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DownloadsViewModel::class.java]
    }

    private val downloadsAdapter by lazy { DownloadsAdapter(::launchPlayer, ::removeTrack) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        downloadsComponent =
            (requireActivity().applicationContext as DownloadsComponentProvider).getDownloadsComponent()
        downloadsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTracks.adapter = downloadsAdapter
        setupSearchView()
        loadDownloadedTracks()

        binding.buttonDownloads.setOnClickListener {
            viewModel.getDownloads()
        }
    }

    private fun loadDownloadedTracks() {

        lifecycleScope.launch {
            viewModel.downloadedTracks.collectLatest { track ->
                with(binding) {
                    if (track.isNotEmpty()) {
                        tracksEmpty.visibility = View.GONE
                        rvTracks.visibility = View.VISIBLE
                        downloadsAdapter.submitList(track)
                    } else {
                        tracksEmpty.visibility = View.VISIBLE
                        rvTracks.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun searchTracks() {

        lifecycleScope.launch {
            viewModel.searchResults.collectLatest { track ->
                with(binding) {
                    if (track.isNotEmpty()) {
                        tracksEmpty.visibility = View.GONE
                        rvTracks.visibility = View.VISIBLE
                        downloadsAdapter.submitList(track)
                    } else {
                        tracksEmpty.visibility = View.VISIBLE
                        rvTracks.visibility = View.GONE
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
                    viewModel.searchTracksFromDownloads(query = query)
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
            DownloadsFragmentDirections.actionDownloadsFragmentToPlayerFragment(trackId = trackId)
        findNavController().navigate(action)
    }

    private fun removeTrack(trackId: Long) {
        viewModel.removeFromDownloads(trackId = trackId)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}