package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.bundle.BundleDetailResponse
import com.example.valorantinfo.data.models.bundle.BundlesListResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface BundleRepository {
    
    suspend fun fetchBundles(): Flow<Resource<BundlesListResponse>>
    
    suspend fun fetchBundleDetails(bundleUuid: String): Flow<Resource<BundleDetailResponse>>
} 