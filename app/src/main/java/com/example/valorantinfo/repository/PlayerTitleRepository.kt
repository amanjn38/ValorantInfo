package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.playertitles.PlayerTitle

interface PlayerTitleRepository {
    suspend fun getPlayerTitles(): List<PlayerTitle>
    suspend fun getPlayerTitle(uuid: String): PlayerTitle?
} 