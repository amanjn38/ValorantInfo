package com.example.valorantinfo.data.models.contracts

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chapter(
    val id: String,
    val position: Int,
    val freeRewards: List<Reward>? = emptyList(),
    val isEpilogue: Boolean,
    val levels: List<Level>
): Parcelable