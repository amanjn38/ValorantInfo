package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.valorantinfo.databinding.FragmentPlayerTitleDetailsBinding
import com.example.valorantinfo.ui.viewmodels.PlayerTitleDetailsUiState
import com.example.valorantinfo.ui.viewmodels.PlayerTitleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerTitleDetailsFragment : Fragment() {

    private var _binding: FragmentPlayerTitleDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerTitleViewModel by viewModels()
    private val args: PlayerTitleDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerTitleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        viewModel.loadPlayerTitle(args.playerTitleUuid)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiStateDetails.collectLatest { state ->
                when (state) {
                    is PlayerTitleDetailsUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.titleCard.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                    }
                    is PlayerTitleDetailsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE

                        val playerTitle = state.playerTitle
                        binding.titleTextTextView.text = playerTitle.titleText
                        binding.displayNameTextView.text = playerTitle.displayName
                        binding.assetPathTextView.text = playerTitle.assetPath

                        binding.titleCard.visibility = View.VISIBLE
                    }
                    is PlayerTitleDetailsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.titleCard.visibility = View.GONE
                        binding.errorTextView.text = state.message
                        binding.errorTextView.visibility = View.VISIBLE
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