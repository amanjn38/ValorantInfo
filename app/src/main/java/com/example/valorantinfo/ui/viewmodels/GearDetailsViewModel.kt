package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.gear.Gear
import com.example.valorantinfo.repository.GearRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GearDetailsViewModel @Inject constructor(
    private val gearRepository: GearRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GearDetailsUiState>(GearDetailsUiState.Loading)
    val uiState: StateFlow<GearDetailsUiState> = _uiState.asStateFlow()

    fun loadGearDetails(gearUuid: String) {
        viewModelScope.launch {
            try {
                _uiState.value = GearDetailsUiState.Loading
                val gear = gearRepository.getGear(gearUuid)
                if (gear == null) {
                    _uiState.value = GearDetailsUiState.Error("Gear not found")
                    return@launch
                }
                _uiState.value = GearDetailsUiState.Success(gear)
            } catch (e: Exception) {
                _uiState.value = GearDetailsUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class GearDetailsUiState {
        object Loading : GearDetailsUiState()
        data class Success(val gear: Gear) : GearDetailsUiState()
        data class Error(val message: String) : GearDetailsUiState()
    }
} 