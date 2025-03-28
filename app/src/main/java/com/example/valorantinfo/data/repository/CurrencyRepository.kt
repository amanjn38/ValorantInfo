package com.example.valorantinfo.data.repository

import com.example.valorantinfo.data.api.ValorantApi
import com.example.valorantinfo.data.models.currencies.Currency
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val api: ValorantApi
) {
    suspend fun getCurrencies(): List<Currency> {
        return try {
            api.getCurrencies().data
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCurrency(uuid: String): Currency? {
        return try {
            api.getCurrency(uuid).data.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
} 