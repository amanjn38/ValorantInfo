package com.example.valorantinfo.data.models.currencies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Currency(
    val uuid: String,
    val displayName: String,
    val displayNameSingular: String,
    val displayIcon: String,
    val largeIcon: String,
    val rewardPreviewIcon: String,
    val assetPath: String
) : Parcelable 