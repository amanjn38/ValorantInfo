package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.agentDetails.AgentDetailsResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface AgentDetailsRepository {
    fun fetchAgentDetails(agentUuid: String): Flow<Resource<AgentDetailsResponse>>
} 