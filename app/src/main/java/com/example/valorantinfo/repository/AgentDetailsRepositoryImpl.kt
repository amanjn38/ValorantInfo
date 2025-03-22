package com.example.valorantinfo.repository

import com.example.valorantinfo.api.AgentDetailsApiService
import com.example.valorantinfo.data.models.agentDetails.AgentDetailsResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AgentDetailsRepositoryImpl @Inject constructor(
    private val apiService: AgentDetailsApiService
) : AgentDetailsRepository {
    override fun fetchAgentDetails(agentUuid: String): Flow<Resource<AgentDetailsResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAgentDetails(agentUuid)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error code: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP error: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.message}"))
        }
    }
} 