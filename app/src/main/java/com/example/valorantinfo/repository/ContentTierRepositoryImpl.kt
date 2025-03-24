package com.example.valorantinfo.repository

import com.example.valorantinfo.data.api.ContentTierApiService
import com.example.valorantinfo.data.models.ContentTier
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContentTierRepositoryImpl @Inject constructor(
    private val api: ContentTierApiService
) : ContentTierRepository {

    override fun getContentTiers(): Flow<Resource<List<ContentTier>>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.getContentTiers()
            emit(Resource.Success(response.data))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    override fun getContentTier(contentTierUuid: String): Flow<Resource<ContentTier>> = flow {
        try {
            emit(Resource.Loading())
            val response = api.getContentTier(contentTierUuid)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
} 