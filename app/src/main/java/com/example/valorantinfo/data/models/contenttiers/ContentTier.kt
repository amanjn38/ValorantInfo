package com.example.valorantinfo.data.models.contenttiers

import com.google.gson.annotations.SerializedName

data class ContentTier(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("devName")
    val devName: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("juiceValue")
    val juiceValue: Int,
    @SerializedName("juiceCost")
    val juiceCost: Int,
    @SerializedName("highlightColor")
    val highlightColor: String,
    @SerializedName("displayIcon")
    val displayIcon: String,
    @SerializedName("assetPath")
    val assetPath: String
) 