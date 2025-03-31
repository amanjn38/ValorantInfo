package com.example.valorantinfo.data.models.playercards

import com.google.gson.annotations.SerializedName

data class PlayerCard(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("displayIcon")
    val displayIcon: String?,
    @SerializedName("largeArt")
    val largeArt: String?,
    @SerializedName("assetPath")
    val assetPath: String
) 