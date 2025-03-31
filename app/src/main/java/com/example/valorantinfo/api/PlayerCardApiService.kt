package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.playercards.PlayerCard
import com.example.valorantinfo.data.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PlayerCardApiService {
    @GET("v1/playercards")
    suspend fun getPlayerCards(): ApiResponse<List<PlayerCard>>

    @GET("v1/playercards/{uuid}")
    suspend fun getPlayerCard(@Path("uuid") uuid: String): ApiResponse<PlayerCard>
}
