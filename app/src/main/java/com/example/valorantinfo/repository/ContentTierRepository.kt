package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.ContentTier
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface ContentTierRepository {
    fun getContentTiers(): Flow<Resource<List<ContentTier>>>
    fun getContentTier(contentTierUuid: String): Flow<Resource<ContentTier>>
} 