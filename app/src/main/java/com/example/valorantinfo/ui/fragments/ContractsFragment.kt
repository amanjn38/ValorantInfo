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
import com.example.valorantinfo.databinding.FragmentContractsBinding
import com.example.valorantinfo.ui.adapters.ContractsAdapter
import com.example.valorantinfo.ui.viewmodels.ContractsViewModel
import com.example.valorantinfo.utilities.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContractsFragment : Fragment() {

    private var _binding: FragmentContractsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContractsViewModel by viewModels()
    private lateinit var contractsAdapter: ContractsAdapter
    private var recyclerViewState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContractsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupToolbar()
        observeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        recyclerViewState = Bundle()
        binding.contractsRecyclerView.layoutManager?.onSaveInstanceState()?.let { state ->
            recyclerViewState?.putParcelable("recycler_state", state)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        recyclerViewState = savedInstanceState
    }

    private fun setupRecyclerView() {
        contractsAdapter = ContractsAdapter { contract ->
            findNavController().navigate(
                ContractsFragmentDirections.actionContractsFragmentToContractChaptersFragment(
                    contractId = contract.uuid,
                ),
            )
        }

        binding.contractsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contractsAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        hideLoading()
                        resource.data?.let { contracts ->
                            if (contracts.isEmpty()) {
                                showError("No contracts available")
                            } else {
                                showContent()
                                contractsAdapter.submitList(contracts)
                            }
                        }
                    }
                    is Resource.Error -> {
                        hideLoading()
                        showError(resource.message ?: "Unknown error occurred")
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressIndicator.visibility = View.VISIBLE
        binding.contractsRecyclerView.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressIndicator.visibility = View.GONE
        binding.contractsRecyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.progressIndicator.visibility = View.GONE
        binding.contractsRecyclerView.visibility = View.GONE
        binding.errorText.apply {
            text = message
            visibility = View.VISIBLE
            setOnClickListener { viewModel.retry() }
        }

        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Retry") { viewModel.retry() }
            .show()
    }

    private fun showContent() {
        binding.contractsRecyclerView.visibility = View.VISIBLE
        binding.progressIndicator.visibility = View.GONE
        binding.errorText.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
