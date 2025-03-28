package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.events.Event
import com.example.valorantinfo.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class EventDetailsUiState {
    object Loading : EventDetailsUiState()
    data class Success(val event: Event) : EventDetailsUiState()
    data class Error(val message: String) : EventDetailsUiState()
}

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val repository: EventRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventDetailsUiState>(EventDetailsUiState.Loading)
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("eventUuid")?.let { uuid ->
            loadEvent(uuid)
        }
    }

    fun loadEvent(uuid: String) {
        viewModelScope.launch {
            _uiState.value = EventDetailsUiState.Loading
            try {
                val event = repository.getEvent(uuid)
                _uiState.value = EventDetailsUiState.Success(event)
            } catch (e: Exception) {
                _uiState.value = EventDetailsUiState.Error(e.message ?: "Failed to load event details")
            }
        }
    }

    fun retry(uuid: String) {
        loadEvent(uuid)
    }
} 