package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.competitivetier.CompetitiveTierDetailResponse
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTiersListResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface CompetitiveTierRepository {
    suspend fun fetchCompetitiveTiers(): Flow<Resource<CompetitiveTiersListResponse>>
    suspend fun fetchCompetitiveTierDetail(competitiveTierUuid: String): Flow<Resource<CompetitiveTierDetailResponse>>
}
