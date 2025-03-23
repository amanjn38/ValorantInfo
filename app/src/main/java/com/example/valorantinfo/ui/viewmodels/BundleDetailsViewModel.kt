package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.bundle.Bundle
import com.example.valorantinfo.data.models.bundle.BundleDetailResponse
import com.example.valorantinfo.repository.BundleRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BundleDetailsViewModel @Inject constructor(
    private val repository: BundleRepository
) : ViewModel() {
    
    private val _bundleDetails = MutableStateFlow<Resource<Bundle>>(Resource.Loading())
    val bundleDetails: StateFlow<Resource<Bundle>> = _bundleDetails
    
    fun fetchBundleDetails(bundleUuid: String) {
        viewModelScope.launch {
            repository.fetchBundleDetails(bundleUuid).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.data?.let { bundle ->
                            _bundleDetails.value = Resource.Success(bundle)
                        } ?: run {
                            _bundleDetails.value = Resource.Error("Bundle data is null")
                        }
                    }
                    is Resource.Error -> {
                        _bundleDetails.value = Resource.Error(result.message ?: "Unknown error")
                    }
                    is Resource.Loading -> {
                        _bundleDetails.value = Resource.Loading()
                    }
                }
            }
        }
    }
} 