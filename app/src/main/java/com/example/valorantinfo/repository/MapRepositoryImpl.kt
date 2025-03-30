package com.example.valorantinfo.repository

import com.example.valorantinfo.api.MapsApiService
import com.example.valorantinfo.data.models.maps.MapDetail
import com.example.valorantinfo.data.models.maps.Maps
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val api: MapsApiService
) : MapRepository {
    override suspend fun getMaps(): Maps {
        return api.getMaps()
    }

    override suspend fun getMap(mapUuid: String): MapDetail {
        return api.getMap(mapUuid)
    }
} 