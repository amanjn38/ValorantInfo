package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.valorantinfo.R
import com.example.valorantinfo.databinding.FragmentBuddiesBinding
import com.example.valorantinfo.ui.adapters.BuddiesAdapter
import com.example.valorantinfo.ui.viewmodels.BuddiesViewModel
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BuddiesFragment : Fragment() {

    private var _binding: FragmentBuddiesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuddiesViewModel by viewModels()
    private lateinit var buddiesAdapter: BuddiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBuddiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchListener()
        observeBuddies()
        observeFilteredBuddies()
    }

    private fun setupRecyclerView() {
        buddiesAdapter = BuddiesAdapter { buddy ->
            // Show a short toast with the buddy name for better feedback
//            Toast.makeText(context, "Loading ${buddy.displayName} details...", Toast.LENGTH_SHORT).show()

            // Navigate to buddy details
            val action = BuddiesFragmentDirections.actionBuddiesFragmentToBuddyDetailsFragment(buddy.uuid)
            findNavController().navigate(action)
        }

        // Set up RecyclerView with optimal column count based on screen width
        val spanCount = calculateSpanCount()
        binding.rvBuddies.apply {
            adapter = buddiesAdapter
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            // Set item animation for better visual feedback
            itemAnimator?.changeDuration = 0
        }
    }

    // Calculate the optimal number of columns based on screen width
    private fun calculateSpanCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return if (screenWidthDp >= 600) 3 else 2 // Use 3 columns for tablets, 2 for phones
    }

    private fun setupSearchListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchBuddies(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Set clear button functionality
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etSearch.text.isNotEmpty()) {
                binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search,
                    0,
                    R.drawable.ic_close,
                    0,
                )
            } else {
                binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search,
                    0,
                    0,
                    0,
                )
            }
        }
    }

    private fun observeBuddies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.buddies.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        binding.rvBuddies.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvBuddies.visibility = View.GONE
                        binding.tvError.text = result.message
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                        binding.rvBuddies.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observeFilteredBuddies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredBuddies.collectLatest { buddies ->
                buddiesAdapter.submitList(buddies)

                // Show empty state message if no buddies match the search
                if (buddies.isEmpty() && viewModel.searchQuery.value.isNotEmpty()) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = "No buddies match your search"
                    binding.rvBuddies.visibility = View.GONE
                } else if (buddies.isNotEmpty()) {
                    binding.tvError.visibility = View.GONE
                    binding.rvBuddies.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
