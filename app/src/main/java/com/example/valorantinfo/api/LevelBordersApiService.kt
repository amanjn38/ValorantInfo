package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.levelborder.LevelBorder
import com.example.valorantinfo.data.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LevelBordersApiService {
    @GET("v1/levelborders")
    suspend fun getLevelBorders(): ApiResponse<List<LevelBorder>>

    @GET("v1/levelborders/{uuid}")
    suspend fun getLevelBorder(@Path("uuid") uuid: String): ApiResponse<LevelBorder>
}
