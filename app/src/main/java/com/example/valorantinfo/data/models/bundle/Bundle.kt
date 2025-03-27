package com.example.valorantinfo.data.models.bundle

data class Bundle(
    val uuid: String,
    val displayName: String,
    val displayNameSubText: String?,
    val description: String,
    val extraDescription: String?,
    val promoDescription: String?,
    val useAdditionalContext: Boolean,
    val displayIcon: String,
    val displayIcon2: String,
    val logoIcon: String?,
    val verticalPromoImage: String,
    val assetPath: String,
)
