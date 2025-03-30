package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.levelborder.LevelBorder
import com.example.valorantinfo.repository.LevelBorderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelBorderViewModel @Inject constructor(
    private val levelBorderRepository: LevelBorderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LevelBorderUiState>(LevelBorderUiState.Loading)
    val uiState: StateFlow<LevelBorderUiState> = _uiState.asStateFlow()

    init {
        loadLevelBorders()
    }

    fun loadLevelBorders() {
        viewModelScope.launch {
            try {
                _uiState.value = LevelBorderUiState.Loading
                val levelBorders = levelBorderRepository.getLevelBorders()
                _uiState.value = LevelBorderUiState.Success(levelBorders)
            } catch (e: Exception) {
                _uiState.value = LevelBorderUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun retry() {
        loadLevelBorders()
    }
}

sealed class LevelBorderUiState {
    object Loading : LevelBorderUiState()
    data class Success(val levelBorders: List<LevelBorder>) : LevelBorderUiState()
    data class Error(val message: String) : LevelBorderUiState()
} 