package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.databinding.FragmentFlexBinding
import com.example.valorantinfo.ui.adapters.FlexAdapter
import com.example.valorantinfo.ui.viewmodels.FlexUiState
import com.example.valorantinfo.ui.viewmodels.FlexViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FlexFragment : Fragment() {

    private var _binding: FragmentFlexBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FlexViewModel by viewModels()
    private lateinit var flexAdapter: FlexAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlexBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        flexAdapter = FlexAdapter {
            // TODO: Navigate to flex details
        }
        binding.rvFlexes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = flexAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is FlexUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                        binding.rvFlexes.visibility = View.GONE
                    }
                    is FlexUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        binding.rvFlexes.visibility = View.VISIBLE
                        flexAdapter.submitList(state.flexes)
                    }
                    is FlexUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvFlexes.visibility = View.GONE
                        binding.tvError.text = state.message
                        binding.tvError.setOnClickListener {
                            viewModel.retry()
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