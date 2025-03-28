package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.flex.Flex
import com.example.valorantinfo.data.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FlexApiService {
    @GET("v1/flex")
    suspend fun getFlexes(): ApiResponse<List<Flex>>

    @GET("v1/flex/{flexUuid}")
    suspend fun getFlex(@Path("flexUuid") flexUuid: String): ApiResponse<Flex>
} 