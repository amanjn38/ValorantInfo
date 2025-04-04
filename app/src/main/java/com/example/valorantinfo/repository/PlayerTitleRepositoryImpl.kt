package com.example.valorantinfo.repository

import com.example.valorantinfo.api.PlayerTitleApiService
import com.example.valorantinfo.data.models.playertitles.PlayerTitle
import javax.inject.Inject

class PlayerTitleRepositoryImpl @Inject constructor(
    private val playerTitleApiService: PlayerTitleApiService
) : PlayerTitleRepository {

    override suspend fun getPlayerTitles(): List<PlayerTitle> {
        return playerTitleApiService.getPlayerTitles().data
    }

    override suspend fun getPlayerTitle(uuid: String): PlayerTitle {
        return playerTitleApiService.getPlayerTitle(uuid).data
    }
} 