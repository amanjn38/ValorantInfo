package com.example.valorantinfo.repository

import com.example.valorantinfo.api.PlayerCardApiService
import com.example.valorantinfo.data.models.playercards.PlayerCard
import javax.inject.Inject

class PlayerCardRepositoryImpl @Inject constructor(
    private val playerCardApiService: PlayerCardApiService
) : PlayerCardRepository {

    override suspend fun getPlayerCards(): List<PlayerCard> {
        return playerCardApiService.getPlayerCards().data
    }

    override suspend fun getPlayerCard(uuid: String): PlayerCard {
        return playerCardApiService.getPlayerCard(uuid).data
    }
}