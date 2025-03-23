package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.bundle.BundlesListResponse
import com.example.valorantinfo.repository.BundleRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BundleViewModel @Inject constructor(
    private val repository: BundleRepository
) : ViewModel() {
    
    private val _bundles = MutableStateFlow<Resource<BundlesListResponse>>(Resource.Loading())
    val bundles: StateFlow<Resource<BundlesListResponse>> = _bundles
    
    init {
        getBundles()
    }
    
    fun getBundles() {
        viewModelScope.launch {
            repository.fetchBundles().collect { result ->
                _bundles.value = result
            }
        }
    }
} 