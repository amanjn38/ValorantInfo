package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.gamemodes.GameMode
import com.example.valorantinfo.repository.GameModeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GameModeUiState {
    object Loading : GameModeUiState()
    data class Success(val gameModes: List<GameMode>) : GameModeUiState()
    data class Error(val message: String) : GameModeUiState()
}

@HiltViewModel
class GameModeViewModel @Inject constructor(
    private val repository: GameModeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameModeUiState>(GameModeUiState.Loading)
    val uiState: StateFlow<GameModeUiState> = _uiState

    init {
        loadGameModes()
    }

    private fun loadGameModes() {
        viewModelScope.launch {
            _uiState.value = GameModeUiState.Loading
            try {
                val gameModes = repository.getGameModes()
                _uiState.value = GameModeUiState.Success(gameModes)
            } catch (e: Exception) {
                _uiState.value = GameModeUiState.Error(e.message ?: "Failed to load game modes")
            }
        }
    }

    fun retry() {
        loadGameModes()
    }
} 