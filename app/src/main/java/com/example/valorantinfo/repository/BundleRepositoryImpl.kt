package com.example.valorantinfo.repository

import com.example.valorantinfo.api.BundleApiService
import com.example.valorantinfo.data.models.bundle.BundleDetailResponse
import com.example.valorantinfo.data.models.bundle.BundlesListResponse
import com.example.valorantinfo.utilities.Constants
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BundleRepositoryImpl @Inject constructor(
    private val bundleApiService: BundleApiService
) : BundleRepository {
    
    override suspend fun fetchBundles(): Flow<Resource<BundlesListResponse>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = bundleApiService.getBundles()
            
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error(Constants.BUNDLE_ERROR_MESSAGE))
            } else {
                emit(Resource.Error(Constants.BUNDLE_ERROR_MESSAGE))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: Constants.BUNDLE_ERROR_MESSAGE))
        }
    }
    
    override suspend fun fetchBundleDetails(bundleUuid: String): Flow<Resource<BundleDetailResponse>> = flow {
        emit(Resource.Loading())
        
        try {
            val response = bundleApiService.getBundleDetails(bundleUuid)
            
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error(Constants.BUNDLE_DETAILS_ERROR_MESSAGE))
            } else {
                emit(Resource.Error(Constants.BUNDLE_DETAILS_ERROR_MESSAGE))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: Constants.BUNDLE_DETAILS_ERROR_MESSAGE))
        }
    }
} 