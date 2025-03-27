package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.contracts.Data
import com.example.valorantinfo.databinding.FragmentContractDetailsBinding
import com.example.valorantinfo.ui.viewmodels.ContractDetailsViewModel
import com.example.valorantinfo.utilities.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "ContractDetailsFragment"

@AndroidEntryPoint
class ContractDetailsFragment : Fragment() {

    private var _binding: FragmentContractDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContractDetailsViewModel by viewModels()
    private val args: ContractDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContractDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "testing onViewCreated called")
        setupToolbar()
        observeViewModel()
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
                        resource.data?.let { contract ->
                            updateUI(contract)
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
        binding.contractImage.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressIndicator.visibility = View.GONE
        binding.contractImage.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.progressIndicator.visibility = View.GONE
        binding.contractImage.visibility = View.GONE
        binding.errorText.apply {
            text = message
            visibility = View.VISIBLE
            setOnClickListener { viewModel.retry() }
        }
    }

    private fun updateUI(contract: Data) {
        Log.d(TAG, "testing Updating UI with contract: ${contract.displayName}")
        binding.apply {
            contractName.text = contract.displayName
            contractDescription.text = contract.content.relationType
            
            // Load contract image
            Glide.with(contractImage)
                .load(contract.displayIcon)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(contractImage)

            // Navigate to chapters list
            val action = ContractDetailsFragmentDirections
                .actionContractDetailsFragmentToContractChaptersFragment(
                    contractId = args.contractId
                )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 