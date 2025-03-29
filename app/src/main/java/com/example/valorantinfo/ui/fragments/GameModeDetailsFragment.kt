package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.gamemodes.GameMode
import com.example.valorantinfo.data.models.gamemodes.GameModeEquippable
import com.example.valorantinfo.databinding.FragmentGameModeDetailsBinding
import com.example.valorantinfo.ui.adapters.EquippableAdapter
import com.example.valorantinfo.ui.viewmodels.GameModeDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameModeDetailsFragment : Fragment() {

    private var _binding: FragmentGameModeDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameModeDetailsViewModel by viewModels()
    private val equippableAdapter = EquippableAdapter()

    private val args: GameModeDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameModeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
        viewModel.loadGameModeDetails(args.gameModeUuid)
    }

    private fun setupRecyclerView() {
        binding.rvEquippables.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = equippableAdapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is GameModeDetailsViewModel.GameModeDetailsUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.nestedScrollView.visibility = View.GONE
                    }
                    is GameModeDetailsViewModel.GameModeDetailsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.nestedScrollView.visibility = View.VISIBLE
                        updateUI(state.gameMode, state.equippables)
                    }
                    is GameModeDetailsViewModel.GameModeDetailsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.nestedScrollView.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                    }
                }
            }
        }
    }

    private fun updateUI(gameMode: GameMode, equippables: List<GameModeEquippable>) {
        binding.apply {
            progressBar.visibility = View.GONE
            nestedScrollView.visibility = View.VISIBLE
            tvError.visibility = View.GONE

            Glide.with(ivGameModeIcon)
                .load(gameMode.displayIcon)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(ivGameModeIcon)

            tvGameModeName.text = gameMode.displayName
            tvGameModeDescription.text = gameMode.description
            tvGameModeDuration.text = getString(R.string.duration_format, gameMode.duration)
            tvGameModeAllowsMatchmaking.text = getString(
                R.string.matchmaking_format,
                getString(if (gameMode.isMatchmakingEnabled) R.string.enabled else R.string.disabled)
            )
            tvGameModeIsTeamVoiceAllowed.text = getString(
                R.string.team_voice_format,
                getString(if (gameMode.isTeamVoiceEnabled) R.string.enabled else R.string.disabled)
            )
            tvGameModeIsMinimapHidden.text = getString(
                R.string.minimap_format,
                getString(if (gameMode.minimapVisibility) R.string.visible else R.string.hidden)
            )
            tvGameModeOrbCount.text = getString(R.string.orb_count_format, gameMode.orbCount)
            
            // Handle null teamRoles safely
            val teamRolesText = gameMode.teamRoles?.joinToString(", ") ?: getString(R.string.not_specified)
            tvGameModeTeamRoles.text = getString(R.string.team_roles_format, teamRolesText)

            equippableAdapter.submitList(equippables)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 