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
import com.example.valorantinfo.databinding.FragmentGearDetailsBinding
import com.example.valorantinfo.ui.viewmodels.GearDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GearDetailsFragment : Fragment() {

    private var _binding: FragmentGearDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GearDetailsViewModel by viewModels()
    private val args: GearDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGearDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        viewModel.loadGearDetails(args.gearUuid)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is GearDetailsViewModel.GearDetailsUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.contentGroup.visibility = View.GONE
                    }
                    is GearDetailsViewModel.GearDetailsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.contentGroup.visibility = View.VISIBLE
                        binding.apply {
                            tvGearName.text = state.gear.displayName
                            tvGearDescription.text = state.gear.description
                            tvGearCost.text = state.gear.shopData!!.cost.toString()
                        }
                    }
                    is GearDetailsViewModel.GearDetailsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.contentGroup.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
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