package com.example.valorantinfo.repository

import com.example.valorantinfo.api.CompetitiveTierApiService
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTierDetailResponse
import com.example.valorantinfo.data.models.competitivetier.CompetitiveTiersListResponse
import com.example.valorantinfo.utilities.Constants
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CompetitiveTierRepositoryImpl @Inject constructor(
    private val apiService: CompetitiveTierApiService
) : CompetitiveTierRepository {

    override suspend fun fetchCompetitiveTiers(): Flow<Resource<CompetitiveTiersListResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getCompetitiveTiers()
            if (response.isSuccessful) {
                response.body()?.let { competitiveTiersResponse ->
                    emit(Resource.Success(competitiveTiersResponse))
                } ?: emit(Resource.Error(Constants.COMPETITIVE_TIERS_ERROR))
            } else {
                emit(Resource.Error(Constants.COMPETITIVE_TIERS_ERROR))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(Constants.COMPETITIVE_TIERS_ERROR))
        } catch (e: IOException) {
            emit(Resource.Error(Constants.NETWORK_ERROR))
        }
    }

    override suspend fun fetchCompetitiveTierDetail(competitiveTierUuid: String): Flow<Resource<CompetitiveTierDetailResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getCompetitiveTierDetail(competitiveTierUuid)
            if (response.isSuccessful) {
                response.body()?.let { competitiveTierDetailResponse ->
                    emit(Resource.Success(competitiveTierDetailResponse))
                } ?: emit(Resource.Error(Constants.COMPETITIVE_TIER_DETAIL_ERROR))
            } else {
                emit(Resource.Error(Constants.COMPETITIVE_TIER_DETAIL_ERROR))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(Constants.COMPETITIVE_TIER_DETAIL_ERROR))
        } catch (e: IOException) {
            emit(Resource.Error(Constants.NETWORK_ERROR))
        }
    }
} 