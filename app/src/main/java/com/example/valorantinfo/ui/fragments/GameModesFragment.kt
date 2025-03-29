package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.data.models.gamemodes.GameMode
import com.example.valorantinfo.databinding.FragmentGameModesBinding
import com.example.valorantinfo.ui.adapters.GameModeAdapter
import com.example.valorantinfo.ui.viewmodels.GameModeUiState
import com.example.valorantinfo.ui.viewmodels.GameModeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameModesFragment : Fragment() {

    private var _binding: FragmentGameModesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameModeViewModel by viewModels()
    private val gameModeAdapter = GameModeAdapter { gameMode ->
        val action = GameModesFragmentDirections.actionGameModesFragmentToGameModeDetailsFragment(gameMode.uuid)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameModesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.rvGameModes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gameModeAdapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is GameModeUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvGameModes.visibility = View.GONE
                    }
                    is GameModeUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvGameModes.visibility = View.VISIBLE
                        gameModeAdapter.submitList(state.gameModes)
                    }
                    is GameModeUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvGameModes.visibility = View.GONE
                        // Handle error state
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 