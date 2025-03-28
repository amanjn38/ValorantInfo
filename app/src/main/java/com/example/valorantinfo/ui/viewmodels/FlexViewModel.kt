package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.flex.Flex
import com.example.valorantinfo.repository.FlexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FlexUiState {
    object Loading : FlexUiState()
    data class Success(val flexes: List<Flex>) : FlexUiState()
    data class Error(val message: String) : FlexUiState()
}

@HiltViewModel
class FlexViewModel @Inject constructor(
    private val repository: FlexRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FlexUiState>(FlexUiState.Loading)
    val uiState: StateFlow<FlexUiState> = _uiState

    init {
        loadFlexes()
    }

    private fun loadFlexes() {
        viewModelScope.launch {
            _uiState.value = FlexUiState.Loading
            try {
                val flexes = repository.getFlexes()
                _uiState.value = FlexUiState.Success(flexes)
            } catch (e: Exception) {
                _uiState.value = FlexUiState.Error(e.message ?: "Failed to load flexes")
            }
        }
    }

    fun retry() {
        loadFlexes()
    }
} 