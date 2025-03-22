package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.agentDetails.AgentDetailsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AgentDetailsApiService {
    @GET("v1/agents/{agentUuid}")
    suspend fun getAgentDetails(@Path("agentUuid") agentUuid: String): Response<AgentDetailsResponse>
} 