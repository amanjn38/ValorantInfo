package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.data.models.currencies.Currency
import com.example.valorantinfo.databinding.FragmentCurrenciesBinding
import com.example.valorantinfo.ui.adapters.CurrencyAdapter
import com.example.valorantinfo.ui.viewmodels.CurrenciesUiState
import com.example.valorantinfo.ui.viewmodels.CurrenciesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrenciesViewModel by viewModels()
    private lateinit var currencyAdapter: CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        currencyAdapter = CurrencyAdapter { currency ->
            showCurrencyDetails(currency)
        }
        binding.rvCurrencies.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currencyAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CurrenciesUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                            binding.rvCurrencies.visibility = View.GONE
                        }
                        is CurrenciesUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                            binding.rvCurrencies.visibility = View.VISIBLE
                            currencyAdapter.submitList(state.currencies)
                        }
                        is CurrenciesUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.rvCurrencies.visibility = View.GONE
                            binding.tvError.text = state.message
                            binding.tvError.setOnClickListener {
                                viewModel.retry()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showCurrencyDetails(currency: Currency) {
        findNavController().navigate(
            CurrenciesFragmentDirections.actionCurrenciesFragmentToCurrencyDetailsFragment(currency.uuid)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 