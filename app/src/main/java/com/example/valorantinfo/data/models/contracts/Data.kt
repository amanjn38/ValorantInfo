package com.example.valorantinfo.data.models.contracts

data class Data(
    val assetPath: String,
    val content: Content,
    val displayIcon: String?,
    val displayName: String?,
    val freeRewardScheduleUuid: String,
    val levelVPCostOverride: Int,
    val shipIt: Boolean,
    val useLevelVPCostOverride: Boolean,
    val uuid: String
)