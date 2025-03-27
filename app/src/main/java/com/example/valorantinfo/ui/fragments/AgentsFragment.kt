package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.valorantinfo.R
import com.example.valorantinfo.databinding.FragmentAgentsBinding
import com.example.valorantinfo.ui.adapters.AgentsAdapter
import com.example.valorantinfo.ui.viewmodels.AgentViewModel
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgentsFragment : Fragment() {

    private var _binding: FragmentAgentsBinding? = null
    private val binding get() = _binding!!

    // Property for testing - allows injecting a mock ViewModel
    internal var viewModelOverride: AgentViewModel? = null

    private val viewModel: AgentViewModel by viewModels()

    private lateinit var agentsAdapter: AgentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAgentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchBar()

        // Use the override viewModel if provided (for testing), otherwise use the injected one
        val activeViewModel = viewModelOverride ?: viewModel

        observeAgents(activeViewModel)
        observeFilteredAgents(activeViewModel)

        // Fetch agents
        activeViewModel.getAgents()
    }

    private fun setupRecyclerView() {
        agentsAdapter = AgentsAdapter { agent ->
            // Navigate to agent details fragment
            val action = AgentsFragmentDirections.actionAgentsFragmentToAgentDetailsFragment(agent.uuid)
            findNavController().navigate(action)
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
            val activeViewModel = viewModelOverride ?: viewModel
            activeViewModel.setSearchQuery(text.toString())
        }
    }

    private fun observeAgents(activeViewModel: AgentViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activeViewModel.agentsState.collectLatest { resource ->
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

    private fun observeFilteredAgents(activeViewModel: AgentViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activeViewModel.filteredAgents.collectLatest { agents ->
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
