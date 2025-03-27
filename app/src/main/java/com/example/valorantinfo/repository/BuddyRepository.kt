package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.buddy.BuddyDetailResponse
import com.example.valorantinfo.data.models.buddy.BuddyLevelResponse
import com.example.valorantinfo.data.models.buddy.BuddyResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface BuddyRepository {
    fun fetchBuddies(): Flow<Resource<BuddyResponse>>
    fun fetchBuddyDetails(buddyUuid: String): Flow<Resource<BuddyDetailResponse>>
    fun fetchBuddyLevel(levelUuid: String): Flow<Resource<BuddyLevelResponse>>
}
