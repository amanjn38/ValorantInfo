package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.flex.Flex
import com.example.valorantinfo.api.FlexApiService
import javax.inject.Inject
import javax.inject.Singleton

interface FlexRepository {
    suspend fun getFlexes(): List<Flex>
    suspend fun getFlex(uuid: String): Flex
}

@Singleton
class FlexRepositoryImpl @Inject constructor(
    private val api: FlexApiService
) : FlexRepository {
    override suspend fun getFlexes(): List<Flex> {
        return try {
            api.getFlexes().data
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getFlex(uuid: String): Flex {
        return try {
            api.getFlex(uuid).data
        } catch (e: Exception) {
            throw e
        }
    }
} 