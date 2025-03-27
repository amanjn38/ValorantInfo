package com.example.valorantinfo.data.models.competitivetier

import com.google.gson.annotations.SerializedName

data class CompetitiveTier(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("assetObjectName") val assetObjectName: String,
    @SerializedName("tiers") val tiers: List<Tier>,
    @SerializedName("assetPath") val assetPath: String,
)

data class Tier(
    @SerializedName("tier") val tier: Int,
    @SerializedName("tierName") val tierName: String,
    @SerializedName("division") val division: String,
    @SerializedName("divisionName") val divisionName: String,
    @SerializedName("color") val color: String,
    @SerializedName("backgroundColor") val backgroundColor: String,
    @SerializedName("smallIcon") val smallIcon: String?,
    @SerializedName("largeIcon") val largeIcon: String?,
    @SerializedName("rankTriangleDownIcon") val rankTriangleDownIcon: String?,
    @SerializedName("rankTriangleUpIcon") val rankTriangleUpIcon: String?,
)
