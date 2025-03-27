package com.example.valorantinfo.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.contracts.Data
import com.example.valorantinfo.repository.ContractRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractsViewModel @Inject constructor(
    private val repository: ContractRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<Data>>>(Resource.Loading())
    val uiState: StateFlow<Resource<List<Data>>> = _uiState.asStateFlow()

    init {
        loadContracts()
    }

    private fun loadContracts() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()

            when (val result = repository.getContracts()) {
                is Resource.Success -> {
                    result.data?.let { contracts ->
                        Log.d("ContractsViewModel", "Loaded ${contracts.data.size} contracts")
                        contracts.data.forEach { contract ->
                            Log.d("ContractsViewModel", "Contract: ${contract.displayName}, Chapters: ${contract.content.chapters.size}")
                        }
                        _uiState.value = Resource.Success(contracts.data)
                    } ?: run {
                        _uiState.value = Resource.Error("Contract data is null")
                    }
                }
                is Resource.Error -> {
                    Log.e("ContractsViewModel", "Error loading contracts: ${result.message}")
                    _uiState.value = Resource.Error(result.message ?: "Unknown error occurred")
                }
                is Resource.Loading -> {
                    _uiState.value = Resource.Loading()
                }
            }
        }
    }

    fun retry() {
        loadContracts()
    }
}
