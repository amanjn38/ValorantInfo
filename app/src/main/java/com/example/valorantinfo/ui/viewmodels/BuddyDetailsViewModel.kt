package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.buddy.Buddy
import com.example.valorantinfo.data.models.buddy.BuddyLevel
import com.example.valorantinfo.repository.BuddyRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuddyDetailsViewModel @Inject constructor(
    private val repository: BuddyRepository
) : ViewModel() {

    private val _buddyDetails = MutableStateFlow<Resource<Buddy>>(Resource.Loading())
    val buddyDetails: StateFlow<Resource<Buddy>> = _buddyDetails
    
    private val _selectedLevel = MutableStateFlow<Resource<BuddyLevel?>>(Resource.Loading())
    val selectedLevel: StateFlow<Resource<BuddyLevel?>> = _selectedLevel

    fun fetchBuddyDetails(buddyUuid: String) {
        viewModelScope.launch {
            repository.fetchBuddyDetails(buddyUuid).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        val buddy = result.data?.data
                        if (buddy != null) {
                            _buddyDetails.value = Resource.Success(buddy)
                        } else {
                            _buddyDetails.value = Resource.Error("Buddy data is null")
                        }
                    }
                    is Resource.Error -> {
                        _buddyDetails.value = Resource.Error(result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {
                        _buddyDetails.value = Resource.Loading()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    
    fun fetchBuddyLevel(levelUuid: String) {
        _selectedLevel.value = Resource.Loading()
        viewModelScope.launch {
            repository.fetchBuddyLevel(levelUuid).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        val level = result.data?.data
                        _selectedLevel.value = Resource.Success(level)
                    }
                    is Resource.Error -> {
                        _selectedLevel.value = Resource.Error(result.message ?: "Failed to load level details")
                    }
                    is Resource.Loading -> {
                        _selectedLevel.value = Resource.Loading()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    
    fun clearSelectedLevel() {
        _selectedLevel.value = Resource.Success(null)
    }
} 