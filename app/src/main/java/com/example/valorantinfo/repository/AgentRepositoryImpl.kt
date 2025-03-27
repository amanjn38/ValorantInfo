package com.example.valorantinfo.repository

import com.example.valorantinfo.api.AgentApiService
import com.example.valorantinfo.data.models.agent.AgentResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AgentRepositoryImpl @Inject constructor(
    private val apiService: AgentApiService,
) : AgentRepository {
    override fun fetchAgents(): Flow<Resource<AgentResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAgents()
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
