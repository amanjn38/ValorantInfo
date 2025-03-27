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
import com.example.valorantinfo.databinding.FragmentCeremoniesBinding
import com.example.valorantinfo.ui.adapters.CeremoniesAdapter
import com.example.valorantinfo.ui.viewmodels.CeremonyViewModel
import com.example.valorantinfo.utilities.Constants
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CeremoniesFragment : Fragment() {

    private var _binding: FragmentCeremoniesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CeremonyViewModel by viewModels()
    private lateinit var ceremoniesAdapter: CeremoniesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCeremoniesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initially hide all content elements
        hideContentElements()

        setupRecyclerView()
        observeCeremonies()
    }

    private fun hideContentElements() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.rvCeremonies.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        ceremoniesAdapter = CeremoniesAdapter { ceremony ->
            // Navigate to ceremony details
            val action = CeremoniesFragmentDirections.actionCeremoniesFragmentToCeremonyDetailsFragment(
                ceremony.uuid,
                ceremony.displayName,
            )
            findNavController().navigate(action)
        }

        binding.rvCeremonies.apply {
            adapter = ceremoniesAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun observeCeremonies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.ceremonies.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        binding.rvCeremonies.visibility = View.VISIBLE

                        result.data?.let { response ->
                            ceremoniesAdapter.submitList(response.data)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvCeremonies.visibility = View.GONE
                        binding.tvError.text = result.message ?: Constants.CEREMONY_ERROR_MESSAGE
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                        binding.rvCeremonies.visibility = View.GONE
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
