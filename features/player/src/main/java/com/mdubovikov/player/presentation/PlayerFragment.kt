package com.mdubovikov.player.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mdubovikov.player.databinding.FragmentPlayerBinding
import com.mdubovikov.player.di.PlayerComponent
import com.mdubovikov.player.di.PlayerComponentProvider
import com.mdubovikov.presentation.ViewModelFactory
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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}