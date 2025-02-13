package com.mdubovikov.downloads.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mdubovikov.downloads.databinding.FragmentDownloadsBinding
import com.mdubovikov.downloads.di.DownloadsComponent
import com.mdubovikov.downloads.di.DownloadsComponentProvider
import com.mdubovikov.downloads.domain.entities.TrackDownloads
import com.mdubovikov.downloads.presentation.adapter.DownloadsAdapter
import com.mdubovikov.presentation.ViewModelFactory
import javax.inject.Inject

class DownloadsFragment : Fragment() {

    private var _binding: FragmentDownloadsBinding? = null
    private val binding: FragmentDownloadsBinding
        get() = _binding ?: throw IllegalStateException("Fragment $this binding cannot be accessed")

    lateinit var downloadsComponent: DownloadsComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DownloadsViewModel::class.java]
    }

    private val downloadsAdapter by lazy { DownloadsAdapter(::onTrackClick, ::switchStatus) }

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
    }

    private fun onTrackClick(track: TrackDownloads) {
    }

    private fun switchStatus(trackId: Long) {
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}