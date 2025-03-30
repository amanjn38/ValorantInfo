package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.maps.Data
import com.example.valorantinfo.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MapsUiState {
    object Loading : MapsUiState()
    data class Success(val maps: List<Data>) : MapsUiState()
    data class Error(val message: String) : MapsUiState()
}

sealed class MapDetailsUiState {
    object Loading : MapDetailsUiState()
    data class Success(val map: Data) : MapDetailsUiState()
    data class Error(val message: String) : MapDetailsUiState()
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {

    private val _mapsUiState = MutableStateFlow<MapsUiState>(MapsUiState.Loading)
    val mapsUiState: StateFlow<MapsUiState> = _mapsUiState.asStateFlow()

    private val _mapDetailsUiState = MutableStateFlow<MapDetailsUiState>(MapDetailsUiState.Loading)
    val mapDetailsUiState: StateFlow<MapDetailsUiState> = _mapDetailsUiState.asStateFlow()

    init {
        loadMaps()
    }

    private fun loadMaps() {
        viewModelScope.launch {
            try {
                _mapsUiState.value = MapsUiState.Loading
                val maps = mapRepository.getMaps()
                _mapsUiState.value = MapsUiState.Success(maps.data)
            } catch (e: Exception) {
                _mapsUiState.value = MapsUiState.Error(e.message ?: "Failed to load maps")
            }
        }
    }

    fun loadMapDetails(mapUuid: String) {
        viewModelScope.launch {
            try {
                _mapDetailsUiState.value = MapDetailsUiState.Loading
                val map = mapRepository.getMap(mapUuid)
                _mapDetailsUiState.value = MapDetailsUiState.Success(map.data)
            } catch (e: Exception) {
                _mapDetailsUiState.value = MapDetailsUiState.Error(e.message ?: "Failed to load map details")
            }
        }
    }

    fun retry() {
        loadMaps()
    }
}