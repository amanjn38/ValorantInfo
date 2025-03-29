package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.gear.GearResponse
import com.example.valorantinfo.data.models.gear.SingleGearResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GearApiService {
    @GET("v1/gear")
    suspend fun getGears(): GearResponse

    @GET("v1/gear/{uuid}")
    suspend fun getGear(@Path("uuid") uuid: String): SingleGearResponse
} 