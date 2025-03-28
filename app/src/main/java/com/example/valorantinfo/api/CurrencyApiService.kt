package com.example.valorantinfo.api

import com.example.valorantinfo.data.models.currencies.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("v1/currencies")
    suspend fun getCurrencies(
        @Query("v1/language") language: String = "en-US"
    ): CurrencyResponse

    @GET("v1/currencies/{currencyUuid}")
    suspend fun getCurrency(
        @Path("currencyUuid") currencyUuid: String,
        @Query("language") language: String = "en-US"
    ): CurrencyResponse
} 