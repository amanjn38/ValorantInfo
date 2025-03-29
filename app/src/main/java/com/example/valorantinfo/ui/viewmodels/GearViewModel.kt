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
class GearViewModel @Inject constructor(
    private val gearRepository: GearRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GearUiState>(GearUiState.Loading)
    val uiState: StateFlow<GearUiState> = _uiState.asStateFlow()

    init {
        loadGear()
    }

    fun loadGear() {
        viewModelScope.launch {
            try {
                _uiState.value = GearUiState.Loading
                val gear = gearRepository.getGears()
                _uiState.value = GearUiState.Success(gear)
            } catch (e: Exception) {
                _uiState.value = GearUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun retry() {
        loadGear()
    }
}

sealed class GearUiState {
    object Loading : GearUiState()
    data class Success(val gear: List<Gear>) : GearUiState()
    data class Error(val message: String) : GearUiState()
} 