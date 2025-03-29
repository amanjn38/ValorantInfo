package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.gamemodes.GameMode
import com.example.valorantinfo.data.models.gamemodes.GameModeEquippable
import com.example.valorantinfo.repository.GameModeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameModeDetailsViewModel @Inject constructor(
    private val repository: GameModeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameModeDetailsUiState>(GameModeDetailsUiState.Loading)
    val uiState: StateFlow<GameModeDetailsUiState> = _uiState.asStateFlow()

    fun loadGameModeDetails(gameModeUuid: String) {
        viewModelScope.launch {
            try {
                _uiState.value = GameModeDetailsUiState.Loading
                val gameMode = repository.getGameMode(gameModeUuid)
                if (gameMode == null) {
                    _uiState.value = GameModeDetailsUiState.Error("Game mode not found")
                    return@launch
                }
                val equippables = repository.getGameModeEquippables()
                _uiState.value = GameModeDetailsUiState.Success(gameMode, equippables)
            } catch (e: Exception) {
                _uiState.value = GameModeDetailsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun retry() {
        _uiState.value?.let { state ->
            if (state is GameModeDetailsUiState.Success) {
                loadGameModeDetails(state.gameMode.uuid)
            }
        }
    }

    sealed class GameModeDetailsUiState {
        object Loading : GameModeDetailsUiState()
        data class Success(
            val gameMode: GameMode,
            val equippables: List<GameModeEquippable>
        ) : GameModeDetailsUiState()
        data class Error(val message: String) : GameModeDetailsUiState()
    }
} 