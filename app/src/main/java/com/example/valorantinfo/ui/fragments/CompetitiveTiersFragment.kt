package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.databinding.FragmentCompetitiveTiersBinding
import com.example.valorantinfo.ui.adapters.CompetitiveTiersAdapter
import com.example.valorantinfo.ui.viewmodels.CompetitiveTierViewModel
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompetitiveTiersFragment : Fragment() {

    private var _binding: FragmentCompetitiveTiersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompetitiveTierViewModel by viewModels()
    private lateinit var adapter: CompetitiveTiersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCompetitiveTiersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = CompetitiveTiersAdapter { competitiveTier ->
            val action = CompetitiveTiersFragmentDirections
                .actionCompetitiveTiersFragmentToCompetitiveTierDetailsFragment(
                    competitiveTier.uuid,
                    competitiveTier.assetObjectName,
                )
            findNavController().navigate(action)
        }

        binding.rvCompetitiveTiers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CompetitiveTiersFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.competitiveTiersState.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        result.data?.data?.let { competitiveTiers ->
                            adapter.submitList(competitiveTiers)
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
