package com.example.valorantinfo.repository

import com.example.valorantinfo.api.EventsApiService
import com.example.valorantinfo.data.models.events.Event
import javax.inject.Inject

interface EventRepository {
    suspend fun getEvents(): List<Event>
    suspend fun getEvent(uuid: String): Event
}

class EventRepositoryImpl @Inject constructor(
    private val api: EventsApiService
) : EventRepository {
    override suspend fun getEvents(): List<Event> {
        return api.getEvents().data
    }

    override suspend fun getEvent(uuid: String): Event {
        return api.getEvent(uuid).data
    }
} 