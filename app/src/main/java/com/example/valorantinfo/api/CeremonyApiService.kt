package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.ceremony.CeremoniesListResponse
import com.example.valorantinfo.data.models.ceremony.CeremonyDetailResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CeremonyApiService {
    @GET("v1/ceremonies")
    suspend fun getCeremonies(): Response<CeremoniesListResponse>

    @GET("v1/ceremonies/{ceremonyUuid}")
    suspend fun getCeremonyDetail(@Path("ceremonyUuid") ceremonyUuid: String): Response<CeremonyDetailResponse>
} 