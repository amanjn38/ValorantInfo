package com.example.valorantinfo.data.models.currencies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyResponse(
    val status: Int,
    val data: List<Currency>
) : Parcelable 