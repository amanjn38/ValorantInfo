package com.example.valorantinfo.data.models.gear

data class GearResponse(
    val status: Int,
    val data: List<Gear>
)

data class SingleGearResponse(
    val status: Int,
    val data: Gear
) 