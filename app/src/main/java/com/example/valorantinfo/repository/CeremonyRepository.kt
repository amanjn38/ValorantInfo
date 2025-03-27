package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.ceremony.CeremoniesListResponse
import com.example.valorantinfo.data.models.ceremony.CeremonyDetailResponse
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow

interface CeremonyRepository {
    suspend fun fetchCeremonies(): Flow<Resource<CeremoniesListResponse>>
    suspend fun fetchCeremonyDetail(ceremonyUuid: String): Flow<Resource<CeremonyDetailResponse>>
}
