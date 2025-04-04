package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.playertitles.PlayerTitle
import com.example.valorantinfo.repository.PlayerTitleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PlayerTitleUiState {
    object Loading : PlayerTitleUiState()
    data class Success(val playerTitles: List<PlayerTitle>) : PlayerTitleUiState()
    data class Error(val message: String) : PlayerTitleUiState()
}

sealed class PlayerTitleDetailsUiState {
    object Loading : PlayerTitleDetailsUiState()
    data class Success(val playerTitle: PlayerTitle) : PlayerTitleDetailsUiState()
    data class Error(val message: String) : PlayerTitleDetailsUiState()
}

@HiltViewModel
class PlayerTitleViewModel @Inject constructor(
    private val playerTitleRepository: PlayerTitleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlayerTitleUiState>(PlayerTitleUiState.Loading)
    val uiState: StateFlow<PlayerTitleUiState> = _uiState.asStateFlow()

    private val _uiStateDetails = MutableStateFlow<PlayerTitleDetailsUiState>(PlayerTitleDetailsUiState.Loading)
    val uiStateDetails: StateFlow<PlayerTitleDetailsUiState> = _uiStateDetails.asStateFlow()

    fun loadPlayerTitles() {
        viewModelScope.launch {
            try {
                _uiState.value = PlayerTitleUiState.Loading
                val playerTitles = playerTitleRepository.getPlayerTitles()
                _uiState.value = PlayerTitleUiState.Success(playerTitles)
            } catch (e: Exception) {
                _uiState.value = PlayerTitleUiState.Error(e.message ?: "Failed to load player titles")
            }
        }
    }

    fun loadPlayerTitle(uuid: String) {
        viewModelScope.launch {
            try {
                _uiStateDetails.value = PlayerTitleDetailsUiState.Loading
                val playerTitle = playerTitleRepository.getPlayerTitle(uuid)
                if (playerTitle != null) {
                    _uiStateDetails.value = PlayerTitleDetailsUiState.Success(playerTitle)
                } else {
                    _uiStateDetails.value = PlayerTitleDetailsUiState.Error("Player title not found")
                }
            } catch (e: Exception) {
                _uiStateDetails.value = PlayerTitleDetailsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
} 