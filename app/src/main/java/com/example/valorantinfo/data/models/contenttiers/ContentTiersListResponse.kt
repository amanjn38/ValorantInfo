package com.example.valorantinfo.data.models.contenttiers

import com.google.gson.annotations.SerializedName

data class ContentTiersListResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: List<ContentTier>
) 