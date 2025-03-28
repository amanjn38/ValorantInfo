package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.events.Event
import com.example.valorantinfo.data.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface EventsApiService {
    @GET("v1/events")
    suspend fun getEvents(): ApiResponse<List<Event>>

    @GET("v1/events/{uuid}")
    suspend fun getEvent(@Path("uuid") uuid: String): ApiResponse<Event>
} 