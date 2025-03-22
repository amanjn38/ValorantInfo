package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.valorantinfo.R
import com.example.valorantinfo.databinding.FragmentAgentsBinding
import com.example.valorantinfo.ui.adapters.AgentsAdapter
import com.example.valorantinfo.ui.viewmodels.AgentViewModel
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Test version of AgentsFragment without Hilt dependencies
 */
class TestAgentsFragment : Fragment() {

    private var _binding: FragmentAgentsBinding? = null
    private val binding get() = _binding!!
    
    lateinit var viewModel: AgentViewModel
    private lateinit var agentsAdapter: AgentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupSearchBar()
        
        observeAgents()
        observeFilteredAgents()
        
        // Fetch agents
        viewModel.getAgents()
    }
    
    private fun setupRecyclerView() {
        agentsAdapter = AgentsAdapter { agent ->
            // No navigation in tests
        }
        binding.rvAgents.apply {
            adapter = agentsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
            itemAnimator = null
        }
    }
    
    private fun setupSearchBar() {
        binding.etSearch.doAfterTextChanged { text ->
            viewModel.setSearchQuery(text.toString())
        }
    }
    
    private fun observeAgents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.agentsState.collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvAgents.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                        }
                        
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvAgents.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                        }
                        
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvAgents.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = resource.message ?: getString(R.string.error_unknown)
                            
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
    
    private fun observeFilteredAgents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredAgents.collectLatest { agents ->
                    agentsAdapter.submitList(agents)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 