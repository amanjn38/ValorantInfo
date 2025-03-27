package com.example.valorantinfo.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
class ContractDetailsViewModel @Inject constructor(
    private val repository: ContractRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val contractId: String = checkNotNull(savedStateHandle["contractId"])

    private val _uiState = MutableStateFlow<Resource<Data>>(Resource.Loading())
    val uiState: StateFlow<Resource<Data>> = _uiState.asStateFlow()

    init {
        loadContractDetails()
    }

    private fun loadContractDetails() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()
            
            when (val result = repository.getContractDetails(contractId)) {
                is Resource.Success -> {
                    result.data?.let { contract ->
                        Log.d("ContractDetails", "Loaded contract: ${contract.displayName}")
                        Log.d("ContractDetails", "Chapters: ${contract.content.chapters.size}")
                        contract.content.chapters.forEachIndexed { index, chapter ->
                            Log.d("ContractDetails", "Chapter ${index + 1}")
                            Log.d("ContractDetails", "Levels: ${chapter.levels.size}")
                            chapter.levels.forEach { level ->
                                Log.d("ContractDetails", "Level XP: ${level.xp}, VP: ${level.vpCost}")
                                Log.d("ContractDetails", "Reward: ${level.reward.type} (${level.reward.amount})")
                            }
                        }
                        _uiState.value = Resource.Success(contract)
                    } ?: run {
                        _uiState.value = Resource.Error("Contract data is null")
                    }
                }
                is Resource.Error -> {
                    Log.e("ContractDetails", "Error loading contract: ${result.message}")
                    _uiState.value = Resource.Error(result.message ?: "Unknown error occurred")
                }
                is Resource.Loading -> {
                    _uiState.value = Resource.Loading()
                }
            }
        }
    }

    fun retry() {
        loadContractDetails()
    }
} 