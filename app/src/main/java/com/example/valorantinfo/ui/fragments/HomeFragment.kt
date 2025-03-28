package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.Category
import com.example.valorantinfo.databinding.FragmentHomeBinding
import com.example.valorantinfo.ui.adapters.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupRecyclerView()
        loadCategories()
    }

    private fun setupClickListeners() {
        binding.apply {
        }
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter { category ->
            when (category.title) {
                "AGENTS" -> findNavController().navigate(R.id.action_homeFragment_to_agentsFragment)
                "BUDDIES" -> findNavController().navigate(R.id.action_homeFragment_to_buddiesFragment)
                "BUNDLES" -> findNavController().navigate(R.id.action_homeFragment_to_bundlesFragment)
                "CEREMONIES" -> findNavController().navigate(R.id.action_homeFragment_to_ceremoniesFragment)
                "COMPETITIVE TIERS" -> findNavController().navigate(R.id.action_homeFragment_to_competitiveTiersFragment)
                "CONTENT TIERS" -> findNavController().navigate(R.id.action_homeFragment_to_contentTiersFragment)
                "CONTRACTS" -> findNavController().navigate(R.id.action_homeFragment_to_contractsFragment)
                "CURRENCIES" -> findNavController().navigate(R.id.action_homeFragment_to_currenciesFragment)
                "EVENTS" -> findNavController().navigate(R.id.action_homeFragment_to_eventsFragment)
                "FLEX" -> findNavController().navigate(R.id.action_homeFragment_to_flexFragment)
                // Add other navigation actions here when you create more fragments
                else -> {
                    // For categories that don't have implementations yet
                }
            }
        }

        binding.rvCategories.apply {
            adapter = categoryAdapter
            // Layout manager is defined in XML
        }
    }

    private fun loadCategories() {
        val categories = listOf(
            Category("AGENTS", getString(R.string.explore_agents)),
            Category("BUDDIES", "Collection of gun buddies to personalize weapons"),
            Category("BUNDLES", "Featured content packs available in the store"),
            Category("CEREMONIES", "Special celebration animations and effects"),
            Category("COMPETITIVE TIERS", getString(R.string.understand_ranks)),
            Category("CONTENT TIERS", "Rarity levels for in-game items and content"),
            Category("CONTRACTS", "Agent contracts and battle pass progression"),
            Category("CURRENCIES", "In-game currencies for purchasing items"),
            Category("EVENTS", "Limited-time events and special occasions"),
            Category("FLEX", "In-game flex"),
            Category("GAMEMODES", getString(R.string.learn_gamemodes)),
            Category("GEAR", "Equipment and utilities available during matches"),
            Category("LEVEL BORDERS", "Player level borders and progression visuals"),
            Category("MAPS", getString(R.string.discover_maps)),
            Category("PLAYER CARDS", "Banner cards for player profiles"),
            Category("PLAYER TITLES", "Special titles displayed on player cards"),
            Category("SEASONS", "Information about competitive seasons"),
            Category("SPRAYS", "Decorative sprays to tag the environment"),
            Category("THEMES", "Visual themes and aesthetics for content"),
            Category("WEAPONS", getString(R.string.browse_weapons)),
            Category("VERSIONS", "Game version history and updates"),
        )

        categoryAdapter.submitList(categories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
