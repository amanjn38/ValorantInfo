package com.example.valorantinfo.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.contracts.Chapter
import com.example.valorantinfo.repository.ContractRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractChaptersViewModel @Inject constructor(
    private val contractRepository: ContractRepository
) : ViewModel() {

    private val TAG = "ContractChaptersVM"
    private val _uiState = MutableLiveData<ContractChaptersUiState>()
    val uiState: LiveData<ContractChaptersUiState> = _uiState

    fun loadChapters(contractId: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "testing Loading chapters for contract: $contractId")
                _uiState.value = ContractChaptersUiState.Loading

                val contract = contractRepository.getContract(contractId)
                if (contract == null) {
                    Log.e(TAG, "testing Contract not found: $contractId")
                    _uiState.value = ContractChaptersUiState.Error("Contract not found")
                    return@launch
                }

                // First, find the epilogue if it exists
                val epilogue = contract.content.chapters.find { it.isEpilogue }
                
                // Then, get all non-epilogue chapters and sort them by position
                val regularChapters = contract.content.chapters
                    .filter { !it.isEpilogue }
                    .sortedBy { it.position }

                // Create the final list with proper IDs and positions
                val chapters = regularChapters.mapIndexed { index, chapter ->
                    Log.d(TAG, "testing Creating chapter ${index + 1} with position $index")
                    chapter.copy(
                        id = "chapter_$index",
                        position = index
                    )
                }.toMutableList()

                // Add epilogue at the end if it exists
                epilogue?.let {
                    Log.d(TAG, "testing Adding epilogue at position ${chapters.size}")
                    chapters.add(it.copy(
                        id = "epilogue",
                        position = chapters.size
                    ))
                }

                Log.d(TAG, "testing Found ${chapters.size} chapters")
                chapters.forEach { chapter ->
                    Log.d(TAG, "testing Chapter ${chapter.position + 1}: ID=${chapter.id}, isEpilogue=${chapter.isEpilogue}, position=${chapter.position}")
                }
                _uiState.value = ContractChaptersUiState.Success(chapters)
            } catch (e: Exception) {
                Log.e(TAG, "testing Error loading chapters", e)
                _uiState.value = ContractChaptersUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

sealed class ContractChaptersUiState {
    object Loading : ContractChaptersUiState()
    data class Success(val chapters: List<Chapter>) : ContractChaptersUiState()
    data class Error(val message: String) : ContractChaptersUiState()
} 