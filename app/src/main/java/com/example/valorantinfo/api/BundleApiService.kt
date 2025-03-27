package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.bundle.BundleDetailResponse
import com.example.valorantinfo.data.models.bundle.BundlesListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BundleApiService {

    @GET("v1/bundles")
    suspend fun getBundles(): Response<BundlesListResponse>

    @GET("v1/bundles/{bundleUuid}")
    suspend fun getBundleDetails(@Path("bundleUuid") bundleUuid: String): Response<BundleDetailResponse>
}
