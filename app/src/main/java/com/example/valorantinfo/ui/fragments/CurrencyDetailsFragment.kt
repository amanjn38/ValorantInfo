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
import com.bumptech.glide.Glide
import com.example.valorantinfo.databinding.FragmentCurrencyDetailsBinding
import com.example.valorantinfo.ui.viewmodels.CurrenciesUiState
import com.example.valorantinfo.ui.viewmodels.CurrenciesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrencyDetailsFragment : Fragment() {

    private var _binding: FragmentCurrencyDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CurrenciesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        
        arguments?.getString("currencyUuid")?.let { uuid ->
            viewModel.loadCurrency(uuid)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CurrenciesUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is CurrenciesUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            state.currencies.firstOrNull()?.let { currency ->
                                binding.apply {
                                    tvCurrencyName.text = currency.displayName
                                    tvCurrencySingular.text = currency.displayNameSingular
                                    tvAssetPath.text = currency.assetPath

                                    Glide.with(ivCurrencyIcon)
                                        .load(currency.displayIcon)
                                        .into(ivCurrencyIcon)
                                }
                            }
                        }
                        is CurrenciesUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
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