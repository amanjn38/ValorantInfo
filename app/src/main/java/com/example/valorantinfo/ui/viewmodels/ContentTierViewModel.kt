package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.ContentTier
import com.example.valorantinfo.repository.ContentTierRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentTierViewModel @Inject constructor(
    private val repository: ContentTierRepository
) : ViewModel() {

    private val _state = MutableStateFlow<Resource<List<ContentTier>>>(Resource.Loading())
    val state: StateFlow<Resource<List<ContentTier>>> = _state.asStateFlow()

    init {
        getContentTiers()
    }

    private fun getContentTiers() {
        viewModelScope.launch {
            repository.getContentTiers().collect { result ->
                _state.value = result
            }
        }
    }
} 