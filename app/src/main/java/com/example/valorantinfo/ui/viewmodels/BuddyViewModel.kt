package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.buddy.Buddy
import com.example.valorantinfo.data.models.buddy.BuddyDetailResponse
import com.example.valorantinfo.data.models.buddy.BuddyResponse
import com.example.valorantinfo.repository.BuddyRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuddyViewModel @Inject constructor(
    private val repository: BuddyRepository,
) : ViewModel() {

    private val _buddiesState = MutableStateFlow<Resource<BuddyResponse>>(Resource.Loading())
    val buddiesState: StateFlow<Resource<BuddyResponse>> = _buddiesState

    private val _buddyDetailState = MutableStateFlow<Resource<BuddyDetailResponse>>(Resource.Loading())
    val buddyDetailState: StateFlow<Resource<BuddyDetailResponse>> = _buddyDetailState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _filteredBuddies = MutableStateFlow<List<Buddy>>(emptyList())
    val filteredBuddies: StateFlow<List<Buddy>> = _filteredBuddies

    private var allBuddies = listOf<Buddy>()

    fun getBuddies() {
        viewModelScope.launch {
            repository.fetchBuddies().collectLatest { resource ->
                _buddiesState.value = resource

                if (resource is Resource.Success) {
                    allBuddies = resource.data?.data ?: emptyList()
                    filterBuddies()
                }
            }
        }
    }

    fun getBuddyDetails(buddyUuid: String) {
        viewModelScope.launch {
            repository.fetchBuddyDetails(buddyUuid).collectLatest { resource ->
                _buddyDetailState.value = resource
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        filterBuddies()
    }

    private fun filterBuddies() {
        val query = _searchQuery.value
        if (query.isEmpty()) {
            _filteredBuddies.value = allBuddies
        } else {
            _filteredBuddies.value = allBuddies.filter {
                it.displayName.contains(query, ignoreCase = true)
            }
        }
    }
}
