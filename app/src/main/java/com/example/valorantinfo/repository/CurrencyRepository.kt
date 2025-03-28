package com.example.valorantinfo.repository

import com.example.valorantinfo.api.CurrencyApiService
import com.example.valorantinfo.data.models.currencies.Currency
import javax.inject.Inject

interface CurrencyRepository {
    suspend fun getCurrencies(): List<Currency>
    suspend fun getCurrency(uuid: String): Currency
}

class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApiService
) : CurrencyRepository {
    override suspend fun getCurrencies(): List<Currency> {
        return api.getCurrencies().data
    }

    override suspend fun getCurrency(uuid: String): Currency {
        return api.getCurrency(uuid).data.get(0)
    }
} 