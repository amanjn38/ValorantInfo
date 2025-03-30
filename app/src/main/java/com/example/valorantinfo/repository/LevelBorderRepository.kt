package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.levelborder.LevelBorder

interface LevelBorderRepository {
    suspend fun getLevelBorders(): List<LevelBorder>
    suspend fun getLevelBorder(uuid: String): LevelBorder
} 