package com.example.valorantinfo.data.models.contracts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reward(
    val amount: Int,
    val isHighlighted: Boolean,
    val type: String,
    val uuid: String,
) : Parcelable
