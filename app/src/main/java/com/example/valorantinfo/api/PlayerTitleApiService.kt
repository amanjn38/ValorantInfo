package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.playertitles.PlayerTitle
import com.example.valorantinfo.data.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PlayerTitleApiService {
    @GET("v1/playertitles")
    suspend fun getPlayerTitles(): ApiResponse<List<PlayerTitle>>

    @GET("v1/playertitles/{uuid}")
    suspend fun getPlayerTitle(@Path("uuid") uuid: String): ApiResponse<PlayerTitle>
} 