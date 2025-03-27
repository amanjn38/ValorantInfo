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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.data.models.contracts.Chapter
import com.example.valorantinfo.databinding.FragmentContractChapterBinding
import com.example.valorantinfo.ui.adapters.ContractLevelAdapter
import com.example.valorantinfo.ui.viewmodels.ContractChapterViewModel
import com.example.valorantinfo.ui.viewmodels.ContractChapterUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "ContractChapterFragment"

@AndroidEntryPoint
class ContractChapterFragment : Fragment() {

    private var _binding: FragmentContractChapterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContractChapterViewModel by viewModels()
    private val args: ContractChapterFragmentArgs by navArgs()
    private lateinit var levelAdapter: ContractLevelAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContractChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "testing onViewCreated called")
        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        loadChapterData()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        levelAdapter = ContractLevelAdapter()
        binding.levelsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = levelAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is ContractChapterUiState.Loading -> {
                        showLoading()
                    }
                    is ContractChapterUiState.Success -> {
                        hideLoading()
                        updateUI(state.chapter)
                    }
                    is ContractChapterUiState.Error -> {
                        hideLoading()
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun loadChapterData() {
        viewModel.loadChapter(
            contractId = args.contractId,
            chapterId = args.chapterId,
            isEpilogue = args.isEpilogue
        )
    }

    private fun showLoading() {
        binding.progressIndicator.visibility = View.VISIBLE
        binding.levelsRecyclerView.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressIndicator.visibility = View.GONE
        binding.levelsRecyclerView.visibility = View.VISIBLE
    }

    private fun showError(message: String) {
        binding.progressIndicator.visibility = View.GONE
        binding.levelsRecyclerView.visibility = View.GONE
        binding.errorText.apply {
            text = message
            visibility = View.VISIBLE
            setOnClickListener { loadChapterData() }
        }
    }

    private fun updateUI(chapter: Chapter) {
        binding.apply {
            chapterTitle.text = "Chapter ${chapter.position + 1}"
            levelAdapter.submitList(chapter.levels)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 