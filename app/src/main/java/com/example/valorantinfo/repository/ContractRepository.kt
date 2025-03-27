package com.example.valorantinfo.repository

import com.example.valorantinfo.data.models.contracts.Contracts
import com.example.valorantinfo.data.models.contracts.Data
import com.example.valorantinfo.utilities.Resource

interface ContractRepository {
    suspend fun getContracts(): Resource<Contracts>
    suspend fun getContractDetails(uuid: String): Resource<Data>
    suspend fun getContract(uuid: String): Data?
} 