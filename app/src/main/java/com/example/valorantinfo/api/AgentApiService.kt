package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.AgentResponse
import retrofit2.Response
import retrofit2.http.GET

interface AgentApiService {
    @GET("v1/agents")
    suspend fun getAgents(): Response<AgentResponse>
}