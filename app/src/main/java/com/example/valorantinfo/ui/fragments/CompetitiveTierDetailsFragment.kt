package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.databinding.FragmentCompetitiveTierDetailsBinding
import com.example.valorantinfo.ui.adapters.TiersAdapter
import com.example.valorantinfo.ui.viewmodels.CompetitiveTierDetailViewModel
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompetitiveTierDetailsFragment : Fragment() {

    private var _binding: FragmentCompetitiveTierDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompetitiveTierDetailViewModel by viewModels()
    private lateinit var adapter: TiersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCompetitiveTierDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupUI()
    }

    private fun setupUI() {
        binding.tvCompetitiveTierName.text = viewModel.getCompetitiveTierName()
    }

    private fun setupRecyclerView() {
        adapter = TiersAdapter()
        binding.rvTiers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CompetitiveTierDetailsFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.competitiveTierDetailState.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        result.data?.data?.let { competitiveTier ->
                            // Sort tiers by their number
                            val sortedTiers = competitiveTier.tiers.sortedBy { it.tier }
                            adapter.submitList(sortedTiers)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = result.message
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
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
