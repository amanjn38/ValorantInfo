package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.valorantinfo.databinding.FragmentMapsBinding
import com.example.valorantinfo.ui.adapters.MapAdapter
import com.example.valorantinfo.ui.viewmodels.MapViewModel
import com.example.valorantinfo.ui.viewmodels.MapsUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapAdapter: MapAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        mapAdapter = MapAdapter { map ->
            findNavController().navigate(
                MapsFragmentDirections.actionMapsFragmentToMapDetailsFragment(map.uuid)
            )
        }
        binding.recyclerView.adapter = mapAdapter
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mapsUiState.collectLatest { state ->
                when (state) {
                    is MapsUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                    }
                    is MapsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.GONE
                        mapAdapter.submitList(state.maps)
                    }
                    is MapsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.errorTextView.apply {
                            visibility = View.VISIBLE
                            text = state.message
                            setOnClickListener { viewModel.retry() }
                        }
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