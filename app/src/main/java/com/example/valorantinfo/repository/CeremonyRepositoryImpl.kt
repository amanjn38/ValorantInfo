package com.example.valorantinfo.repository

import com.example.valorantinfo.api.CeremonyApiService
import com.example.valorantinfo.data.models.ceremony.CeremoniesListResponse
import com.example.valorantinfo.data.models.ceremony.CeremonyDetailResponse
import com.example.valorantinfo.utilities.Constants
import com.example.valorantinfo.utilities.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CeremonyRepositoryImpl @Inject constructor(
    private val ceremonyApiService: CeremonyApiService,
) : CeremonyRepository {

    override suspend fun fetchCeremonies(): Flow<Resource<CeremoniesListResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = ceremonyApiService.getCeremonies()
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    emit(Resource.Success(result))
                } ?: emit(Resource.Error(Constants.ERROR_EMPTY_RESPONSE))
            } else {
                emit(Resource.Error("${response.code()} ${response.message()}"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(Constants.ERROR_NETWORK_CONNECTION))
        } catch (e: HttpException) {
            emit(Resource.Error(Constants.ERROR_HTTP_EXCEPTION))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: Constants.ERROR_UNKNOWN))
        }
    }

    override suspend fun fetchCeremonyDetail(ceremonyUuid: String): Flow<Resource<CeremonyDetailResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = ceremonyApiService.getCeremonyDetail(ceremonyUuid)
            if (response.isSuccessful) {
                response.body()?.let { result ->
                    emit(Resource.Success(result))
                } ?: emit(Resource.Error(Constants.ERROR_EMPTY_RESPONSE))
            } else {
                emit(Resource.Error("${response.code()} ${response.message()}"))
            }
        } catch (e: IOException) {
            emit(Resource.Error(Constants.ERROR_NETWORK_CONNECTION))
        } catch (e: HttpException) {
            emit(Resource.Error(Constants.ERROR_HTTP_EXCEPTION))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: Constants.ERROR_UNKNOWN))
        }
    }
}
