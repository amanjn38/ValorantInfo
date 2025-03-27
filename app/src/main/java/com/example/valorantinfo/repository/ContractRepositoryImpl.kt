package com.example.valorantinfo.repository

import com.example.valorantinfo.api.ContractApiService
import com.example.valorantinfo.data.models.contracts.Contracts
import com.example.valorantinfo.data.models.contracts.Data
import com.example.valorantinfo.utilities.Resource
import javax.inject.Inject

class ContractRepositoryImpl @Inject constructor(
    private val api: ContractApiService,
) : ContractRepository {
    override suspend fun getContracts(): Resource<Contracts> {
        return try {
            val response = api.getContracts()
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getContractDetails(uuid: String): Resource<Data> {
        return try {
            System.out.println("testing1222 " + uuid)
            val response = api.getContractDetails(uuid)
            System.out.println("testing12222 " + response)
            Resource.Success(response.data)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun getContract(uuid: String): Data? {
        return try {
            val response = api.getContractDetails(uuid)
            response.data
        } catch (e: Exception) {
            null
        }
    }
}
