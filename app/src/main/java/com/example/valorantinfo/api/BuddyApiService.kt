package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.buddy.BuddyDetailResponse
import com.example.valorantinfo.data.models.buddy.BuddyLevelResponse
import com.example.valorantinfo.data.models.buddy.BuddyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BuddyApiService {
    @GET("v1/buddies")
    suspend fun getBuddies(): Response<BuddyResponse>

    @GET("v1/buddies/{buddyUuid}")
    suspend fun getBuddyDetails(@Path("buddyUuid") buddyUuid: String): Response<BuddyDetailResponse>

    @GET("v1/buddies/levels/{buddyLevelUuid}")
    suspend fun getBuddyLevel(@Path("buddyLevelUuid") buddyLevelUuid: String): Response<BuddyLevelResponse>
}
