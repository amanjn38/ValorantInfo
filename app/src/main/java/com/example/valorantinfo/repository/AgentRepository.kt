package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.AgentResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface AgentRepository {
    fun fetchAgents(): Flow<Resource<AgentResponse>>
}