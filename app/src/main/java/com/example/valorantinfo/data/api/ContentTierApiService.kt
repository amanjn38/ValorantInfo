package com.example.valorantinfo.data.api

import com.example.valorantinfo.data.models.ContentTier
import com.example.valorantinfo.data.models.ContentTiersListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ContentTierApiService {
    @GET("contenttiers")
    suspend fun getContentTiers(): ContentTiersListResponse

    @GET("contenttiers/{contentTierUuid}")
    suspend fun getContentTier(@Path("contentTierUuid") contentTierUuid: String): ContentTier
} 