package com.example.valorantinfo.data.models.competitivetier

import com.google.gson.annotations.SerializedName

data class CompetitiveTierDetailResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: CompetitiveTier,
)
