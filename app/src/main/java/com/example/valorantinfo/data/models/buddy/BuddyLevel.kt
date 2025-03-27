package com.example.valorantinfo.data.models.buddy

data class BuddyLevel(
    val uuid: String,
    val charmLevel: Int,
    val hideIfNotOwned: Boolean,
    val displayName: String,
    val displayIcon: String,
    val assetPath: String,
)
