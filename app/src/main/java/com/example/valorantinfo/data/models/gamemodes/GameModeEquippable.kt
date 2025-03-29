package com.example.valorantinfo.data.models.gamemodes

import com.google.gson.annotations.SerializedName

data class GameModeEquippable(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("displayIcon")
    val displayIcon: String?,
    @SerializedName("assetPath")
    val assetPath: String
) 