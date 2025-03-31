package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.valorantinfo.databinding.FragmentPlayerCardBinding
import com.example.valorantinfo.ui.adapters.PlayerCardAdapter
import com.example.valorantinfo.ui.layoutmanagers.LazyGridLayoutManager
import com.example.valorantinfo.ui.viewmodels.PlayerCardUiState
import com.example.valorantinfo.ui.viewmodels.PlayerCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlayerCardFragment : Fragment() {

    private var _binding: FragmentPlayerCardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlayerCardViewModel by viewModels()
    private lateinit var adapter: PlayerCardAdapter
    private lateinit var layoutManager: LazyGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
        viewModel.loadPlayerCard()
    }

    private fun setupRecyclerView() {
        adapter = PlayerCardAdapter { playerCard ->
            findNavController().navigate(
                PlayerCardFragmentDirections.actionPlayerCardFragmentToPlayerCardDetailsFragment(
                    playerCard.uuid
                )
            )
        }

        layoutManager = LazyGridLayoutManager(requireContext(), 2).apply {
            setOnLoadMoreListener(object : LazyGridLayoutManager.OnLoadMoreListener {
                override fun onLoadMore() {
                    // Prefetch next set of images
                    viewModel.prefetchNextImages()
                }
            })
        }

        binding.recyclerView.apply {
            layoutManager = this@PlayerCardFragment.layoutManager
            adapter = this@PlayerCardFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is PlayerCardUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                    }
                    is PlayerCardUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.GONE
                        adapter.submitList(state.playerCard)
                    }
                    is PlayerCardUiState.Error -> {
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