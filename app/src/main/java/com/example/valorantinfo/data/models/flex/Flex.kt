package com.example.valorantinfo.data.models.flex

import com.google.gson.annotations.SerializedName

data class Flex(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("displayNameAllCaps")
    val displayNameAllCaps: String,
    @SerializedName("displayIcon")
    val displayIcon: String,
    @SerializedName("assetPath")
    val assetPath: String
) 