package com.example.valorantinfo.data.models.contracts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Level(
    val doughCost: Int,
    val isPurchasableWithDough: Boolean,
    val isPurchasableWithVP: Boolean,
    val reward: Reward,
    val vpCost: Int,
    val xp: Int,
) : Parcelable
