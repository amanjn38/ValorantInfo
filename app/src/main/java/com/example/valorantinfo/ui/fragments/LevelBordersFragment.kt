package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.valorantinfo.databinding.FragmentLevelBordersBinding
import com.example.valorantinfo.ui.adapters.LevelBorderAdapter
import com.example.valorantinfo.ui.viewmodels.LevelBorderUiState
import com.example.valorantinfo.ui.viewmodels.LevelBorderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LevelBordersFragment : Fragment() {

    private var _binding: FragmentLevelBordersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LevelBorderViewModel by viewModels()
    private lateinit var adapter: LevelBorderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelBordersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        adapter = LevelBorderAdapter { levelBorder ->
            findNavController().navigate(
                LevelBordersFragmentDirections.actionLevelBordersFragmentToLevelBorderDetailsFragment(
                    levelBorder.uuid
                )
            )
        }
        binding.recyclerView.adapter = adapter
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is LevelBorderUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                    }
                    is LevelBorderUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.GONE
                        adapter.submitList(state.levelBorders)
                    }
                    is LevelBorderUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.errorTextView.visibility = View.VISIBLE
                        binding.errorTextView.text = state.message
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