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
class LevelBorderDetailsViewModel @Inject constructor(
    private val levelBorderRepository: LevelBorderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LevelBorderDetailsUiState>(LevelBorderDetailsUiState.Loading)
    val uiState: StateFlow<LevelBorderDetailsUiState> = _uiState.asStateFlow()

    fun loadLevelBorder(uuid: String) {
        viewModelScope.launch {
            try {
                _uiState.value = LevelBorderDetailsUiState.Loading
                val levelBorder = levelBorderRepository.getLevelBorder(uuid)
                _uiState.value = LevelBorderDetailsUiState.Success(levelBorder)
            } catch (e: Exception) {
                _uiState.value = LevelBorderDetailsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun retry() {
        // Implement retry logic if needed
    }
}

sealed class LevelBorderDetailsUiState {
    object Loading : LevelBorderDetailsUiState()
    data class Success(val levelBorder: LevelBorder) : LevelBorderDetailsUiState()
    data class Error(val message: String) : LevelBorderDetailsUiState()
} 