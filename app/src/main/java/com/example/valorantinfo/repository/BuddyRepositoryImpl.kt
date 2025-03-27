package com.example.valorantinfo.repository

import com.example.valorantinfo.api.BuddyApiService
import com.example.valorantinfo.data.models.buddy.BuddyDetailResponse
import com.example.valorantinfo.data.models.buddy.BuddyLevelResponse
import com.example.valorantinfo.data.models.buddy.BuddyResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BuddyRepositoryImpl @Inject constructor(
    private val apiService: BuddyApiService,
) : BuddyRepository {

    override fun fetchBuddies(): Flow<Resource<BuddyResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getBuddies()
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

    override fun fetchBuddyDetails(buddyUuid: String): Flow<Resource<BuddyDetailResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getBuddyDetails(buddyUuid)
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

    override fun fetchBuddyLevel(levelUuid: String): Flow<Resource<BuddyLevelResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getBuddyLevel(levelUuid)
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
