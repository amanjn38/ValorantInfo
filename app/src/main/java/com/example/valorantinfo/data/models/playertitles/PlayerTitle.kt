package com.example.valorantinfo.data.models.playertitles

import com.google.gson.annotations.SerializedName

data class PlayerTitle(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("titleText")
    val titleText: String,
    @SerializedName("isHiddenIfNotOwned")
    val isHiddenIfNotOwned: Boolean,
    @SerializedName("assetPath")
    val assetPath: String
) 