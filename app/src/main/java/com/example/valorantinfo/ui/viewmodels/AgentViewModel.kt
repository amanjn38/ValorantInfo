package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.agent.Agent
import com.example.valorantinfo.data.models.agent.AgentResponse
import com.example.valorantinfo.repository.AgentRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentViewModel @Inject constructor(
    private val repository: AgentRepository
) : ViewModel() {
    private val _agentsState = MutableStateFlow<Resource<AgentResponse>>(Resource.Loading())
    val agentsState: StateFlow<Resource<AgentResponse>> get() = _agentsState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _filteredAgents = MutableStateFlow<List<Agent>>(emptyList())
    val filteredAgents: StateFlow<List<Agent>> get() = _filteredAgents

    private var allAgents = listOf<Agent>()

    fun getAgents() {
        viewModelScope.launch {
            repository.fetchAgents().collect { resource ->
                _agentsState.value = resource
                
                if (resource is Resource.Success) {
                    allAgents = resource.data?.data?.filter { it.isPlayableCharacter } ?: emptyList()
                    filterAgents()
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        filterAgents()
    }

    private fun filterAgents() {
        val query = _searchQuery.value
        if (query.isEmpty()) {
            _filteredAgents.value = allAgents
        } else {
            _filteredAgents.value = allAgents.filter {
                it.displayName.contains(query, ignoreCase = true) ||
                it.role?.displayName?.contains(query, ignoreCase = true) == true
            }
        }
    }
}