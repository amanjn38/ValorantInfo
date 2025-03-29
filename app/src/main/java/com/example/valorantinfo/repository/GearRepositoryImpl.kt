package com.example.valorantinfo.repository

import com.example.valorantinfo.api.GearApiService
import com.example.valorantinfo.data.models.gear.Gear
import javax.inject.Inject

class GearRepositoryImpl @Inject constructor(
    private val api: GearApiService
) : GearRepository {

    override suspend fun getGears(): List<Gear> {
        return api.getGears().data
    }

    override suspend fun getGear(uuid: String): Gear? {
        return api.getGear(uuid).data
    }
} 