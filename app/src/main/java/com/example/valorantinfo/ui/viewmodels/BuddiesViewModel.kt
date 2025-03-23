package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.buddy.Buddy
import com.example.valorantinfo.repository.BuddyRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class BuddiesViewModel @Inject constructor(
    private val repository: BuddyRepository
) : ViewModel() {

    private val _buddies = MutableStateFlow<Resource<List<Buddy>>>(Resource.Loading())
    val buddies: StateFlow<Resource<List<Buddy>>> = _buddies

    private val _filteredBuddies = MutableStateFlow<List<Buddy>>(emptyList())
    val filteredBuddies: StateFlow<List<Buddy>> = _filteredBuddies
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var originalBuddiesList: List<Buddy> = emptyList()

    init {
        fetchBuddies()
        // Set up debounced search to improve performance
        setupSearchQueryListener()
    }

    private fun setupSearchQueryListener() {
        _searchQuery
            .debounce(300) // Debounce to avoid searching on every keystroke
            .distinctUntilChanged() // Only emit when the query actually changes
            .map { query -> 
                if (query.isEmpty()) {
                    originalBuddiesList
                } else {
                    originalBuddiesList.filter {
                        it.displayName.contains(query, ignoreCase = true)
                    }
                }
            }
            .onEach { filtered ->
                _filteredBuddies.value = filtered
            }
            .launchIn(viewModelScope)
    }

    fun fetchBuddies() {
        viewModelScope.launch {
            repository.fetchBuddies().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        val buddyList = result.data?.data?.filter { it.isHiddenIfNotOwned.not() } ?: emptyList()
                        // Sort alphabetically for better user experience
                        val sortedList = buddyList.sortedBy { it.displayName }
                        originalBuddiesList = sortedList
                        _filteredBuddies.value = sortedList
                        _buddies.value = Resource.Success(sortedList)
                    }
                    is Resource.Error -> {
                        _buddies.value = Resource.Error(result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {
                        _buddies.value = Resource.Loading()
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun searchBuddies(query: String) {
        _searchQuery.value = query
    }
} 