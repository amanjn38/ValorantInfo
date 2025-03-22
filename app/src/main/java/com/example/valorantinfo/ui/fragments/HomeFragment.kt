package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.Category
import com.example.valorantinfo.databinding.FragmentHomeBinding
import com.example.valorantinfo.ui.adapters.CategoryAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        loadCategories()
    }
    
    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter { category ->
            when (category.title) {
                "Agents" -> findNavController().navigate(R.id.action_homeFragment_to_agentsFragment)
                // Add other navigation actions here when you create more fragments
                else -> {
                    // For categories that don't have implementations yet
                }
            }
        }
        
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    
    private fun loadCategories() {
        val categories = listOf(
            Category("Agents", R.drawable.ic_agent_placeholder),
            Category("Buddies", R.drawable.ic_agent_placeholder),
            Category("Bundles", R.drawable.ic_agent_placeholder),
            Category("Ceremonies", R.drawable.ic_agent_placeholder),
            Category("Competitive Tiers", R.drawable.ic_agent_placeholder),
            Category("Content Tiers", R.drawable.ic_agent_placeholder),
            Category("Contracts", R.drawable.ic_agent_placeholder),
            Category("Currencies", R.drawable.ic_agent_placeholder),
            Category("Events", R.drawable.ic_agent_placeholder),
            Category("Gamemodes", R.drawable.ic_agent_placeholder),
            Category("Gear", R.drawable.ic_agent_placeholder),
            Category("Level Borders", R.drawable.ic_agent_placeholder),
            Category("Maps", R.drawable.ic_agent_placeholder),
            Category("Player Cards", R.drawable.ic_agent_placeholder),
            Category("Player Titles", R.drawable.ic_agent_placeholder),
            Category("Seasons", R.drawable.ic_agent_placeholder),
            Category("Sprays", R.drawable.ic_agent_placeholder),
            Category("Themes", R.drawable.ic_agent_placeholder),
            Category("Weapons", R.drawable.ic_agent_placeholder),
            Category("Versions", R.drawable.ic_agent_placeholder)
        )
        
        categoryAdapter.submitList(categories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}