package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.currencies.Currency
import com.example.valorantinfo.data.repository.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CurrenciesUiState>(CurrenciesUiState.Loading)
    val uiState: StateFlow<CurrenciesUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    fun loadCurrencies() {
        viewModelScope.launch {
            _uiState.value = CurrenciesUiState.Loading
            try {
                val currencies = repository.getCurrencies()
                _uiState.value = CurrenciesUiState.Success(currencies)
            } catch (e: Exception) {
                _uiState.value = CurrenciesUiState.Error(e.message ?: "Failed to load currencies")
            }
        }
    }

    fun loadCurrency(uuid: String) {
        viewModelScope.launch {
            _uiState.value = CurrenciesUiState.Loading
            try {
                val currency = repository.getCurrency(uuid)
                if (currency != null) {
                    _uiState.value = CurrenciesUiState.Success(listOf(currency))
                } else {
                    _uiState.value = CurrenciesUiState.Error("Currency not found")
                }
            } catch (e: Exception) {
                _uiState.value = CurrenciesUiState.Error(e.message ?: "Failed to load currency")
            }
        }
    }

    fun retry() {
        loadCurrencies()
    }
}

sealed class CurrenciesUiState {
    data object Loading : CurrenciesUiState()
    data class Success(val currencies: List<Currency>) : CurrenciesUiState()
    data class Error(val message: String) : CurrenciesUiState()
} 