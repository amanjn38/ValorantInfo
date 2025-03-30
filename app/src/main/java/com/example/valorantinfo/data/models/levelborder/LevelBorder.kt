package com.example.valorantinfo.data.models.levelborder

import com.google.gson.annotations.SerializedName

data class LevelBorder(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("startingLevel")
    val startingLevel: Int,
    @SerializedName("levelNumberAppearance")
    val levelNumberAppearance: String,
    @SerializedName("smallPlayerCardAppearance")
    val smallPlayerCardAppearance: String,
    @SerializedName("assetPath")
    val assetPath: String
) 