package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.valorantinfo.data.models.levelborder.LevelBorder
import com.example.valorantinfo.databinding.FragmentLevelBorderDetailsBinding
import com.example.valorantinfo.ui.viewmodels.LevelBorderDetailsUiState
import com.example.valorantinfo.ui.viewmodels.LevelBorderDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LevelBorderDetailsFragment : Fragment() {

    private var _binding: FragmentLevelBorderDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LevelBorderDetailsViewModel by viewModels()
    private val args: LevelBorderDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelBorderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        viewModel.loadLevelBorder(args.levelBorderUuid)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is LevelBorderDetailsUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.GONE
                    }
                    is LevelBorderDetailsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                        updateUI(state.levelBorder)
                    }
                    is LevelBorderDetailsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorTextView.visibility = View.VISIBLE
                        binding.errorTextView.text = state.message
                    }
                }
            }
        }
    }

    private fun updateUI(levelBorder: LevelBorder) {
        binding.apply {
            nameTextView.text = levelBorder.displayName
            levelTextView.text = "Level ${levelBorder.startingLevel}"
            assetPathTextView.text = levelBorder.assetPath

            Glide.with(this@LevelBorderDetailsFragment)
                .load(levelBorder.levelNumberAppearance)
                .into(borderImageView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 