package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.playercards.PlayerCard

interface PlayerCardRepository {
    suspend fun getPlayerCards(): List<PlayerCard>
    suspend fun getPlayerCard(uuid: String): PlayerCard?
}
