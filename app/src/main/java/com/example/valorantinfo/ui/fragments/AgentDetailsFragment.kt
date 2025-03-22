package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.valorantinfo.databinding.FragmentAgentDetailsBinding
import com.example.valorantinfo.ui.adapters.AbilitiesAdapter
import com.example.valorantinfo.ui.viewmodels.AgentDetailsViewModel
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgentDetailsFragment : Fragment() {

    private var _binding: FragmentAgentDetailsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: AgentDetailsViewModel by viewModels()
    private val args: AgentDetailsFragmentArgs by navArgs()
    private lateinit var abilitiesAdapter: AbilitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeAgentDetails()
        
        // Fetch agent details
        viewModel.getAgentDetails(args.agentUuid)
    }
    
    private fun setupRecyclerView() {
        abilitiesAdapter = AbilitiesAdapter()
        binding.rvAbilities.apply {
            adapter = abilitiesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    
    private fun observeAgentDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.agentDetailsState.collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                        }
                        
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                            
                            resource.data?.data?.let { agentDetails ->
                                // Set agent name and description
                                binding.tvAgentName.text = agentDetails.displayName
                                binding.tvAgentDescription.text = agentDetails.description
                                
                                // Load agent portrait
                                Glide.with(requireContext())
                                    .load(agentDetails.fullPortrait ?: agentDetails.displayIcon)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(binding.ivAgentPortrait)
                                
                                // Load agent background
                                agentDetails.background?.let { backgroundUrl ->
                                    Glide.with(requireContext())
                                        .load(backgroundUrl)
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(binding.ivAgentBackground)
                                }
                                
                                // Set agent role if available
                                agentDetails.role?.let { role ->
                                    binding.tvAgentRole.text = role.displayName
                                    
                                    // Load role icon
                                    Glide.with(requireContext())
                                        .load(role.displayIcon)
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(binding.ivAgentRole)
                                }
                                
                                // Set agent abilities
                                agentDetails.abilities?.let { abilities ->
                                    abilitiesAdapter.submitList(abilities)
                                }
                            }
                        }
                        
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = resource.message ?: "Unknown error occurred"
                            
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
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