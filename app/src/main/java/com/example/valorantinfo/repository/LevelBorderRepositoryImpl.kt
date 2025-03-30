package com.example.valorantinfo.repository

import com.example.valorantinfo.api.LevelBordersApiService
import com.example.valorantinfo.data.models.levelborder.LevelBorder
import javax.inject.Inject

class LevelBorderRepositoryImpl @Inject constructor(
    private val api: LevelBordersApiService
) : LevelBorderRepository {
    override suspend fun getLevelBorders(): List<LevelBorder> {
        return api.getLevelBorders().data
    }

    override suspend fun getLevelBorder(uuid: String): LevelBorder {
        return api.getLevelBorder(uuid).data
    }
} 