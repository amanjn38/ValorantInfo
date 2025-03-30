package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.maps.MapDetail
import com.example.valorantinfo.data.models.maps.Maps
import retrofit2.http.GET
import retrofit2.http.Path

interface MapsApiService {
    @GET("v1/maps")
    suspend fun getMaps(): Maps

    @GET("v1/maps/{uuid}")
    suspend fun getMap(@Path("uuid") uuid: String): MapDetail
}
