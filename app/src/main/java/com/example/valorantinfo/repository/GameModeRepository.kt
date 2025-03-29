package com.example.valorantinfo.repository

import com.example.valorantinfo.api.GameModeApiService
import com.example.valorantinfo.data.models.gamemodes.GameMode
import com.example.valorantinfo.data.models.gamemodes.GameModeEquippable
import javax.inject.Inject
import javax.inject.Singleton

interface GameModeRepository {
    suspend fun getGameModes(): List<GameMode>
    suspend fun getGameMode(uuid: String): GameMode
    suspend fun getGameModeEquippables(): List<GameModeEquippable>
    suspend fun getGameModeEquippable(uuid: String): GameModeEquippable
}
