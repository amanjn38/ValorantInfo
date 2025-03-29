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
import com.example.valorantinfo.databinding.FragmentGearBinding
import com.example.valorantinfo.ui.adapters.GearAdapter
import com.example.valorantinfo.ui.viewmodels.GearUiState
import com.example.valorantinfo.ui.viewmodels.GearViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GearFragment : Fragment() {

    private var _binding: FragmentGearBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GearViewModel by viewModels()
    private val gearAdapter = GearAdapter { gear ->
        navigateToGearDetails(gear.uuid)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGearBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.rvGear.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = gearAdapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is GearUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.nestedScrollView.visibility = View.GONE
                    }
                    is GearUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.nestedScrollView.visibility = View.VISIBLE
                        gearAdapter.submitList(state.gear)
                    }
                    is GearUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.nestedScrollView.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = state.message
                    }
                }
            }
        }
    }

    private fun navigateToGearDetails(gearUuid: String) {
        val action = GearFragmentDirections.actionGearFragmentToGearDetailsFragment(gearUuid)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 