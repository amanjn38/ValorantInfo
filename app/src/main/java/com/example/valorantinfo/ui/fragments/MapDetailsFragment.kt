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
import com.example.valorantinfo.data.models.maps.Data
import com.example.valorantinfo.databinding.FragmentMapDetailsBinding
import com.example.valorantinfo.ui.adapters.CalloutAdapter
import com.example.valorantinfo.ui.viewmodels.MapDetailsUiState
import com.example.valorantinfo.ui.viewmodels.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapDetailsFragment : Fragment() {

    private var _binding: FragmentMapDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()
    private val args: MapDetailsFragmentArgs by navArgs()
    private lateinit var calloutAdapter: CalloutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
        viewModel.loadMapDetails(args.mapUuid)
    }

    private fun setupRecyclerView() {
        calloutAdapter = CalloutAdapter()
        binding.calloutsRecyclerView.adapter = calloutAdapter
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mapDetailsUiState.collectLatest { state ->
                when (state) {
                    is MapDetailsUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.root.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                    }
                    is MapDetailsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.root.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.GONE
                        updateUI(state.map)
                    }
                    is MapDetailsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.root.visibility = View.GONE
                        binding.errorTextView.apply {
                            visibility = View.VISIBLE
                            text = state.message
                            setOnClickListener { viewModel.loadMapDetails(args.mapUuid) }
                        }
                    }
                }
            }
        }
    }
    private fun updateUI(map: Data) {
        binding.apply {
            mapNameTextView.text = map.displayName
            mapDescriptionTextView.text = map.tacticalDescription
            coordinatesTextView.text = "Coordinates: ${map.coordinates}"

            Glide.with(mapImageView)
                .load(map.splash)
                .centerCrop()
                .into(mapImageView)

            calloutAdapter.submitList(map.callouts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 