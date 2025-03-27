package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.valorantinfo.databinding.FragmentBundlesBinding
import com.example.valorantinfo.ui.adapters.BundlesAdapter
import com.example.valorantinfo.ui.viewmodels.BundleViewModel
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BundlesFragment : Fragment() {

    private var _binding: FragmentBundlesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BundleViewModel by viewModels()
    private lateinit var bundlesAdapter: BundlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBundlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initially hide all content elements
        hideContentElements()

        setupRecyclerView()
        observeBundles()
    }

    private fun hideContentElements() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.rvBundles.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        bundlesAdapter = BundlesAdapter { bundle ->
            // Navigate to bundle details
            val action = BundlesFragmentDirections.actionBundlesFragmentToBundleDetailsFragment(
                bundle.uuid,
                bundle.displayName,
            )
            findNavController().navigate(action)
        }

        binding.rvBundles.apply {
            adapter = bundlesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observeBundles() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bundles.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        binding.rvBundles.visibility = View.VISIBLE

                        result.data?.let { response ->
                            bundlesAdapter.submitList(response.data)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvBundles.visibility = View.GONE
                        binding.tvError.text = result.message
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                        binding.rvBundles.visibility = View.GONE
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
