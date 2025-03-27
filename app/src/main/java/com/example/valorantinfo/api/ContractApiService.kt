package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.contracts.ContractDetail
import com.example.valorantinfo.data.models.contracts.Contracts
import retrofit2.http.GET
import retrofit2.http.Path

interface ContractApiService {
    @GET("v1/contracts")
    suspend fun getContracts(): Contracts

    @GET("v1/contracts/{uuid}")
    suspend fun getContractDetails(@Path("uuid") uuid: String): ContractDetail
}
