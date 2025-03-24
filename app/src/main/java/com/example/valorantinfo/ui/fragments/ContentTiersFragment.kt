package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.databinding.FragmentContentTiersBinding
import com.example.valorantinfo.ui.adapters.ContentTiersAdapter
import com.example.valorantinfo.ui.viewmodels.ContentTierViewModel
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContentTiersFragment : Fragment() {

    private var _binding: FragmentContentTiersBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ContentTierViewModel by viewModels()
    private lateinit var adapter: ContentTiersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentTiersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        adapter = ContentTiersAdapter { contentTier ->
            // Handle item click if needed
            Toast.makeText(context, "Selected: ${contentTier.displayName}", Toast.LENGTH_SHORT).show()
        }
        
        binding.rvContentTiers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ContentTiersFragment.adapter
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        adapter.submitList(result.data)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.apply {
                            text = result.message
                            visibility = View.VISIBLE
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