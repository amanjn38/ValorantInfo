package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.valorantinfo.databinding.FragmentCeremonyDetailsBinding
import com.example.valorantinfo.ui.viewmodels.CeremonyDetailViewModel
import com.example.valorantinfo.utilities.Constants
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CeremonyDetailsFragment : Fragment() {

    private var _binding: FragmentCeremonyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CeremonyDetailViewModel by viewModels()
    private val args: CeremonyDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCeremonyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initially hide all content elements
        hideContentElements()

        viewModel.fetchCeremonyDetail(args.ceremonyUuid)
        observeCeremonyDetails()
    }

    private fun hideContentElements() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE

        // Hide all content sections until data is loaded
        binding.tvCeremonyName.visibility = View.GONE
        binding.divider.visibility = View.GONE
        binding.tvTypeLabel.visibility = View.GONE
        binding.tvCeremonyType.visibility = View.GONE
        binding.tvAssetLabel.visibility = View.GONE
        binding.tvAssetPath.visibility = View.GONE
        binding.tvCeremonyDescription.visibility = View.GONE
        binding.tvCeremonyUuid.visibility = View.GONE
    }

    private fun observeCeremonyDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ceremonyDetail.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE

                        result.data?.let { response ->
                            val ceremony = response.data

                            // Show all content sections
                            binding.tvCeremonyName.visibility = View.VISIBLE
                            binding.divider.visibility = View.VISIBLE
                            binding.tvTypeLabel.visibility = View.VISIBLE
                            binding.tvCeremonyType.visibility = View.VISIBLE
                            binding.tvAssetLabel.visibility = View.VISIBLE
                            binding.tvAssetPath.visibility = View.VISIBLE
                            binding.tvCeremonyDescription.visibility = View.VISIBLE
                            binding.tvCeremonyUuid.visibility = View.VISIBLE

                            // Set data
                            binding.tvCeremonyName.text = ceremony.displayName
                            binding.tvAssetPath.text = ceremony.assetPath
                            binding.tvCeremonyUuid.text = "UUID: ${ceremony.uuid}"
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = result.message ?: Constants.CEREMONY_DETAILS_ERROR_MESSAGE

                        // Hide all content sections
                        binding.tvCeremonyName.visibility = View.GONE
                        binding.divider.visibility = View.GONE
                        binding.tvTypeLabel.visibility = View.GONE
                        binding.tvCeremonyType.visibility = View.GONE
                        binding.tvAssetLabel.visibility = View.GONE
                        binding.tvAssetPath.visibility = View.GONE
                        binding.tvCeremonyDescription.visibility = View.GONE
                        binding.tvCeremonyUuid.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE

                        // Keep content hidden during loading
                        binding.tvCeremonyName.visibility = View.GONE
                        binding.divider.visibility = View.GONE
                        binding.tvTypeLabel.visibility = View.GONE
                        binding.tvCeremonyType.visibility = View.GONE
                        binding.tvAssetLabel.visibility = View.GONE
                        binding.tvAssetPath.visibility = View.GONE
                        binding.tvCeremonyDescription.visibility = View.GONE
                        binding.tvCeremonyUuid.visibility = View.GONE
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
