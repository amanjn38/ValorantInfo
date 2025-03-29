package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.gear.Gear

interface GearRepository {
    suspend fun getGears(): List<Gear>
    suspend fun getGear(uuid: String): Gear?
} 