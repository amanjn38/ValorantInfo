package com.example.valorantinfo.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valorantinfo.data.models.agentDetails.AgentDetailsResponse
import com.example.valorantinfo.repository.AgentDetailsRepository
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgentDetailsViewModel @Inject constructor(
    private val repository: AgentDetailsRepository,
) : ViewModel() {
    private val _agentDetailsState = MutableStateFlow<Resource<AgentDetailsResponse>>(Resource.Loading())
    val agentDetailsState: StateFlow<Resource<AgentDetailsResponse>> get() = _agentDetailsState

    fun getAgentDetails(agentUuid: String) {
        viewModelScope.launch {
            repository.fetchAgentDetails(agentUuid).collect {
                _agentDetailsState.value = it
            }
        }
    }
}
