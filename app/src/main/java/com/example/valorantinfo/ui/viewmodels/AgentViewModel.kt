package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getAgents() {
        viewModelScope.launch {
            repository.fetchAgents().collect {
                _agentsState.value = it
            }
        }
    }
}