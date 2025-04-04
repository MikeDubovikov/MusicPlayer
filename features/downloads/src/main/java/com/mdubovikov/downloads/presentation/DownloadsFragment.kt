package com.mdubovikov.downloads.presentation

import android.content.Context
import android.os.Bundle
import android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mdubovikov.downloads.DownloadsRouter
import com.mdubovikov.downloads.databinding.FragmentDownloadsBinding
import com.mdubovikov.downloads.di.DownloadsComponent
import com.mdubovikov.downloads.di.DownloadsComponentProvider
import com.mdubovikov.downloads.domain.entities.TrackDownloads
import com.mdubovikov.downloads.presentation.adapter.DownloadsAdapter
import com.mdubovikov.theme.R
import com.mdubovikov.util.BaseFragment
import com.mdubovikov.util.ViewModelFactory
import com.mdubovikov.util.observeStateOn
import javax.inject.Inject

class DownloadsFragment : BaseFragment<FragmentDownloadsBinding>(), DownloadsRouter {

    private lateinit var downloadsComponent: DownloadsComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DownloadsViewModel::class.java]
    }

    private val downloadsAdapter by lazy { DownloadsAdapter(::launchPlayer) }

    override fun createBinding(): FragmentDownloadsBinding {
        return FragmentDownloadsBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        downloadsComponent =
            (requireActivity().applicationContext as DownloadsComponentProvider).getDownloadsComponent()
        downloadsComponent.inject(this)
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
        viewModel.downloadedTracks.observeStateOn(viewLifecycleOwner) { track ->
            updateTracksList(track)
        }
    }

    private fun searchTracks() {
        viewModel.searchResults.observeStateOn(viewLifecycleOwner) { track ->
            updateTracksList(track)
        }
    }

    private fun updateTracksList(track: List<TrackDownloads>) {
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

    private fun setupSearchView() {

        val searchIcon =
            binding.svMeals.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setImageResource(R.drawable.ic_search)

        val searchEditText =
            binding.svMeals.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setHintTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.dark_gray
            )
        )
        searchEditText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
        searchEditText.textSize = 14f

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
}