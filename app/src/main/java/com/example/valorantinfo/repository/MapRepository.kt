package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.maps.MapDetail
import com.example.valorantinfo.data.models.maps.Maps

interface MapRepository {
    suspend fun getMaps(): Maps
    suspend fun getMap(mapUuid: String): MapDetail
}
