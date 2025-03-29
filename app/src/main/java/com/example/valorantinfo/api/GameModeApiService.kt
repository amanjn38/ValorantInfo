package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.gamemodes.GameMode
import com.example.valorantinfo.data.models.gamemodes.GameModeEquippable
import com.example.valorantinfo.data.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GameModeApiService {
    @GET("v1/gamemodes")
    suspend fun getGameModes(): ApiResponse<List<GameMode>>

    @GET("v1/gamemodes/{gameModeUuid}")
    suspend fun getGameMode(@Path("gameModeUuid") gameModeUuid: String): ApiResponse<GameMode>

    @GET("v1/gamemodes/equippables")
    suspend fun getGameModeEquippables(): ApiResponse<List<GameModeEquippable>>

    @GET("v1/gamemodes/equippables/{gameModeEquippableUuid}")
    suspend fun getGameModeEquippable(@Path("gameModeEquippableUuid") gameModeEquippableUuid: String): ApiResponse<GameModeEquippable>
} 