package com.example.valorantinfo.ui.viewmodels

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

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventsUiState>(EventsUiState.Loading)
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = EventsUiState.Loading
            try {
                val events = repository.getEvents()
                _uiState.value = EventsUiState.Success(events)
            } catch (e: Exception) {
                _uiState.value = EventsUiState.Error(e.message ?: "Failed to load events")
            }
        }
    }

    fun loadEvent(uuid: String) {
        viewModelScope.launch {
            _uiState.value = EventsUiState.Loading
            try {
                val event = repository.getEvent(uuid)
                if (event != null) {
                    _uiState.value = EventsUiState.Success(listOf(event))
                } else {
                    _uiState.value = EventsUiState.Error("Event not found")
                }
            } catch (e: Exception) {
                _uiState.value = EventsUiState.Error(e.message ?: "Failed to load event")
            }
        }
    }

    fun retry() {
        loadEvents()
    }
}

sealed class EventsUiState {
    data object Loading : EventsUiState()
    data class Success(val events: List<Event>) : EventsUiState()
    data class Error(val message: String) : EventsUiState()
} 