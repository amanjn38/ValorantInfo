package com.example.valorantinfo.repository

import com.example.valorantinfo.api.GameModeApiService
import com.example.valorantinfo.data.models.gamemodes.GameMode
import com.example.valorantinfo.data.models.gamemodes.GameModeEquippable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameModeRepositoryImpl @Inject constructor(
    private val api: GameModeApiService
) : GameModeRepository {
    override suspend fun getGameModes(): List<GameMode> {
        return try {
            api.getGameModes().data
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getGameMode(uuid: String): GameMode {
        return try {
            api.getGameMode(uuid).data
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getGameModeEquippables(): List<GameModeEquippable> {
        return try {
            api.getGameModeEquippables().data
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getGameModeEquippable(uuid: String): GameModeEquippable {
        return try {
            api.getGameModeEquippable(uuid).data
        } catch (e: Exception) {
            throw e
        }
    }
} 