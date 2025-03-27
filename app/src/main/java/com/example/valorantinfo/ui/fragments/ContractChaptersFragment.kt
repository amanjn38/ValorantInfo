package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.data.models.contracts.Chapter
import com.example.valorantinfo.databinding.FragmentContractChaptersBinding
import com.example.valorantinfo.ui.adapters.ContractChaptersAdapter
import com.example.valorantinfo.ui.viewmodels.ContractChaptersUiState
import com.example.valorantinfo.ui.viewmodels.ContractChaptersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "ContractChaptersFragment"

@AndroidEntryPoint
class ContractChaptersFragment : Fragment() {

    private var _binding: FragmentContractChaptersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContractChaptersViewModel by viewModels()
    private lateinit var chaptersAdapter: ContractChaptersAdapter
    private val args: ContractChaptersFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContractChaptersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "testing onViewCreated called")

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        viewModel.loadChapters(args.contractId)
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "testing Setting up RecyclerView")
        chaptersAdapter = ContractChaptersAdapter { chapter ->
            navigateToChapterLevels(chapter)
        }

        binding.chaptersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chaptersAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        Log.d(TAG, "testing Starting to observe ViewModel")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.observe(viewLifecycleOwner) { state ->
                    Log.d(TAG, "testing Received UI state: $state")
                    when (state) {
                        is ContractChaptersUiState.Loading -> {
                            showLoading()
                        }
                        is ContractChaptersUiState.Success -> {
                            hideLoading()
                            chaptersAdapter.submitList(state.chapters)
                        }
                        is ContractChaptersUiState.Error -> {
                            showError(state.message)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressIndicator.visibility = View.VISIBLE
        binding.chaptersRecyclerView.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressIndicator.visibility = View.GONE
        binding.chaptersRecyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.progressIndicator.visibility = View.GONE
        binding.chaptersRecyclerView.visibility = View.GONE
        binding.errorText.apply {
            text = message
            visibility = View.VISIBLE
            setOnClickListener { viewModel.loadChapters(args.contractId) }
        }
    }

    private fun navigateToChapterLevels(chapter: Chapter) {
        val action = ContractChaptersFragmentDirections
            .actionContractChaptersFragmentToContractChapterFragment(
                contractId = args.contractId,
                chapterId = chapter.id,
                isEpilogue = chapter.isEpilogue
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 