package com.example.valorantinfo.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.contracts.Chapter
import com.example.valorantinfo.repository.ContractRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ContractChapterViewModel"

sealed class ContractChapterUiState {
    object Loading : ContractChapterUiState()
    data class Success(val chapter: Chapter) : ContractChapterUiState()
    data class Error(val message: String) : ContractChapterUiState()
}

@HiltViewModel
class ContractChapterViewModel @Inject constructor(
    private val repository: ContractRepository,
) : ViewModel() {

    private val _uiState = MutableLiveData<ContractChapterUiState>()
    val uiState: LiveData<ContractChapterUiState> = _uiState

    private var currentChapter: Chapter? = null

    fun loadChapter(contractId: String, chapterId: String, isEpilogue: Boolean) {
        Log.d(TAG, "testing Loading chapter data for contractId: $contractId, chapterId: $chapterId, isEpilogue: $isEpilogue")
        viewModelScope.launch {
            try {
                _uiState.value = ContractChapterUiState.Loading
                when (val result = repository.getContractDetails(contractId)) {
                    is Resource.Success -> {
                        result.data?.let { contract ->
                            Log.d(TAG, "testing Contract loaded: ${contract.displayName}")
                            Log.d(TAG, "testing Total chapters: ${contract.content.chapters.size}")

                            val chapter = if (isEpilogue) {
                                Log.d(TAG, "testing Looking for epilogue")
                                contract.content.chapters.find { it.isEpilogue }
                            } else {
                                val position = chapterId.split("_")[1].toInt()
                                Log.d(TAG, "testing Looking for chapter at position: $position")
                                contract.content.chapters
                                    .filter { !it.isEpilogue }
                                    .sortedBy { it.position }
                                    .getOrNull(position)
                            }

                            if (chapter != null) {
                                Log.d(TAG, "testing Found chapter: position=${chapter.position}, isEpilogue=${chapter.isEpilogue}")
                                currentChapter = chapter
                                _uiState.value = ContractChapterUiState.Success(chapter)
                            } else {
                                Log.e(TAG, "testing Chapter not found. Available chapters: ${contract.content.chapters.map { it.position }}")
                                _uiState.value = ContractChapterUiState.Error("Chapter not found")
                            }
                        } ?: run {
                            Log.e(TAG, "testing Contract data is null")
                            _uiState.value = ContractChapterUiState.Error("Contract data is null")
                        }
                    }
                    is Resource.Error -> {
                        Log.e(TAG, "testing Error loading contract: ${result.message}")
                        _uiState.value = result.message?.let { ContractChapterUiState.Error(it) }
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "testing Loading contract data...")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "testing Error loading chapter", e)
                _uiState.value = ContractChapterUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentChapter = null
    }
}
