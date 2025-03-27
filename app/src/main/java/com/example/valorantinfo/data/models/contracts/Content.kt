package com.example.valorantinfo.data.models.contracts

data class Content(
    val chapters: List<Chapter>,
    val premiumRewardScheduleUuid: Any,
    val premiumVPCost: Int,
    val relationType: String,
    val relationUuid: String,
)
