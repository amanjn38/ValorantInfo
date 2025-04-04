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
import com.example.valorantinfo.databinding.FragmentPlayerTitleBinding
import com.example.valorantinfo.ui.adapters.PlayerTitleAdapter
import com.example.valorantinfo.ui.viewmodels.PlayerTitleUiState
import com.example.valorantinfo.ui.viewmodels.PlayerTitleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerTitleFragment : Fragment() {

    private var _binding: FragmentPlayerTitleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerTitleViewModel by viewModels()
    private lateinit var adapter: PlayerTitleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
        viewModel.loadPlayerTitles()
    }

    private fun setupRecyclerView() {
        adapter = PlayerTitleAdapter { playerTitle ->
            findNavController().navigate(
                PlayerTitleFragmentDirections.actionPlayerTitleFragmentToPlayerTitleDetailsFragment(
                    playerTitle.uuid
                )
            )
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PlayerTitleFragment.adapter
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is PlayerTitleUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                    }
                    is PlayerTitleUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.GONE
                        adapter.submitList(state.playerTitles)
                    }
                    is PlayerTitleUiState.Error -> {
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