package com.example.valorantinfo.data.models.events

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("shortDisplayName")
    val shortDisplayName: String,
    @SerializedName("startTime")
    val startTime: String,
    @SerializedName("endTime")
    val endTime: String,
    @SerializedName("assetPath")
    val assetPath: String
) 