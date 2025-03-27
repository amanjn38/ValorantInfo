package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.contenttiers.ContentTier
import com.example.valorantinfo.data.models.contenttiers.ContentTiersListResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ContentTierApiService {
    @GET("v1/contenttiers")
    suspend fun getContentTiers(): ContentTiersListResponse

    @GET("v1/contenttiers/{contentTierUuid}")
    suspend fun getContentTier(@Path("contentTierUuid") contentTierUuid: String): ContentTier
}
