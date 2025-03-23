package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.valorantinfo.data.models.buddy.BuddyLevel
import com.example.valorantinfo.databinding.FragmentBuddyDetailsBinding
import com.example.valorantinfo.ui.adapters.BuddyLevelsAdapter
import com.example.valorantinfo.ui.viewmodels.BuddyDetailsViewModel
import com.example.valorantinfo.utilities.Resource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BuddyDetailsFragment : Fragment() {

    private var _binding: FragmentBuddyDetailsBinding? = null
    // Made binding accessible for testing
    internal val binding get() = _binding!!

    val viewModel: BuddyDetailsViewModel by viewModels()
    private val args: BuddyDetailsFragmentArgs by navArgs()
    private lateinit var buddyLevelsAdapter: BuddyLevelsAdapter
    // Made bottomSheetBehavior accessible for testing
    internal lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuddyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initially hide all content elements
        hideContentElements()
        
        setupBottomSheet()
        setupRecyclerView()
        viewModel.fetchBuddyDetails(args.buddyUuid)
        observeBuddyDetails()
        observeSelectedLevel()
    }
    
    // Added helper method to hide content elements
    private fun hideContentElements() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        
        // Hide all content sections until data is loaded
        binding.tvBuddyName.visibility = View.GONE
        binding.ivBuddyIcon.visibility = View.GONE
        binding.tvDescriptionLabel.visibility = View.GONE
        binding.tvBuddyDescription.visibility = View.GONE
        binding.tvLevelsLabel.visibility = View.GONE
        binding.tvLevelsHint.visibility = View.GONE
        binding.rvBuddyLevels.visibility = View.GONE
        binding.bottomSheetContainer.visibility = View.GONE
    }

    // Made the method accessible for testing
    internal fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        
        binding.btnCloseBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.clearSelectedLevel()
        }
        
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    viewModel.clearSelectedLevel()
                }
            }
            
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Not needed
            }
        })
    }

    private fun setupRecyclerView() {
        buddyLevelsAdapter = BuddyLevelsAdapter { level ->
            // Handle level click
            showLevelDetails(level)
        }
        binding.rvBuddyLevels.apply {
            adapter = buddyLevelsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    
    // Made the method accessible for testing
    private fun showLevelDetails(level: BuddyLevel) {
        // Pre-populate UI with data we already have
        binding.tvLevelDetailName.text = level.displayName.uppercase()
        binding.tvLevelDetailCharmLevel.text = "CHARM LEVEL: ${level.charmLevel}"
        binding.tvLevelUuid.text = "UUID: ${level.uuid}"
        
        Glide.with(requireContext())
            .load(level.displayIcon)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.ivLevelDetailIcon)
            
        // Show the bottom sheet
        binding.bottomSheetContainer.visibility = View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        
        // Fetch detailed level data from API
        viewModel.fetchBuddyLevel(level.uuid)
    }

    private fun observeBuddyDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.buddyDetails.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        
                        result.data?.let { buddy ->
                            // Show main elements
                            binding.tvBuddyName.visibility = View.VISIBLE
                            binding.ivBuddyIcon.visibility = View.VISIBLE
                            
                            binding.tvBuddyName.text = buddy.displayName
                            
                            // Load buddy icon
                            Glide.with(requireContext())
                                .load(buddy.displayIcon)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.ivBuddyIcon)
                            
                            // Make description labels visible and set description
                            binding.tvDescriptionLabel.visibility = View.VISIBLE
                            binding.tvBuddyDescription.visibility = View.VISIBLE
                            
                            // Set description if available (using level description)
                            if (buddy.levels.isNotEmpty()) {
                                val defaultLevel = buddy.levels.first()
                                binding.tvBuddyDescription.text = defaultLevel.displayName
                            } else {
                                binding.tvBuddyDescription.text = "No description available"
                            }

                            // Set levels list
                            if (buddy.levels.isNotEmpty()) {
                                binding.tvLevelsLabel.visibility = View.VISIBLE
                                binding.tvLevelsHint.visibility = View.VISIBLE
                                binding.rvBuddyLevels.visibility = View.VISIBLE
                                buddyLevelsAdapter.submitList(buddy.levels)
                            } else {
                                binding.tvLevelsLabel.visibility = View.GONE
                                binding.tvLevelsHint.visibility = View.GONE
                                binding.rvBuddyLevels.visibility = View.GONE
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = result.message
                        
                        // Hide content sections
                        binding.tvBuddyName.visibility = View.GONE
                        binding.ivBuddyIcon.visibility = View.GONE
                        binding.tvDescriptionLabel.visibility = View.GONE
                        binding.tvBuddyDescription.visibility = View.GONE
                        binding.tvLevelsLabel.visibility = View.GONE
                        binding.tvLevelsHint.visibility = View.GONE
                        binding.rvBuddyLevels.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                        
                        // Hide content sections during loading
                        binding.tvBuddyName.visibility = View.GONE
                        binding.ivBuddyIcon.visibility = View.GONE
                        binding.tvDescriptionLabel.visibility = View.GONE
                        binding.tvBuddyDescription.visibility = View.GONE
                        binding.tvLevelsLabel.visibility = View.GONE
                        binding.tvLevelsHint.visibility = View.GONE
                        binding.rvBuddyLevels.visibility = View.GONE
                    }
                }
            }
        }
    }
    
    private fun observeSelectedLevel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedLevel.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.pbLevelDetails.visibility = View.GONE
                        binding.tvLevelDetailError.visibility = View.GONE
                        
                        // We've already pre-populated the UI, 
                        // but here we could update with any additional data from the API response
                        result.data?.let { level ->
                            binding.tvLevelDetailName.text = level.displayName.uppercase()
                            binding.tvLevelDetailCharmLevel.text = "CHARM LEVEL: ${level.charmLevel}"
                            binding.tvLevelUuid.text = "UUID: ${level.uuid}"
                            
                            Glide.with(requireContext())
                                .load(level.displayIcon)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(binding.ivLevelDetailIcon)
                        }
                    }
                    is Resource.Error -> {
                        binding.pbLevelDetails.visibility = View.GONE
                        binding.tvLevelDetailError.visibility = View.VISIBLE
                        binding.tvLevelDetailError.text = result.message
                    }
                    is Resource.Loading -> {
                        binding.pbLevelDetails.visibility = View.VISIBLE
                        binding.tvLevelDetailError.visibility = View.GONE
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