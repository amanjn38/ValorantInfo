package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.competitivetier.CompetitiveTierDetailResponse
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTiersListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CompetitiveTierApiService {
    @GET("v1/competitivetiers")
    suspend fun getCompetitiveTiers(): Response<CompetitiveTiersListResponse>

    @GET("v1/competitivetiers/{competitiveTierUuid}")
    suspend fun getCompetitiveTierDetail(
        @Path("competitiveTierUuid") competitiveTierUuid: String,
    ): Response<CompetitiveTierDetailResponse>
}
