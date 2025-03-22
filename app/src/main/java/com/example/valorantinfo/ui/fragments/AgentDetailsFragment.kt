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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.agentDetails.AgentDetails
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
                            binding.contentGroup.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                        }
                        
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.contentGroup.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                            
                            resource.data?.data?.let { agentDetails ->
                                setupAgent(agentDetails)
                            }
                        }
                        
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.contentGroup.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = resource.message ?: getString(R.string.error_unknown)
                            
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupAgent(agentDetails: AgentDetails) {
        binding.apply {
            tvAgentName.text = agentDetails.displayName
            tvAgentDescription.text = agentDetails.description

            val role = agentDetails.role
            if (role != null) {
                tvAgentRole.text = role.displayName
                Glide.with(requireContext())
                    .load(role.displayIcon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivAgentRole)
            }

            Glide.with(requireContext())
                .load(agentDetails.fullPortrait)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivAgentPortrait)

            Glide.with(requireContext())
                .load(agentDetails.background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivAgentBackground)

            val abilitiesAdapter = AbilitiesAdapter()
            rvAbilities.apply {
                adapter = abilitiesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                itemAnimator = null
            }
            abilitiesAdapter.submitList(agentDetails.abilities)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 