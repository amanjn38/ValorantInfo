package com.example.valorantinfo.data.models.buddy

data class Buddy(
    val uuid: String,
    val displayName: String,
    val isHiddenIfNotOwned: Boolean,
    val themeUuid: String?,
    val displayIcon: String,
    val assetPath: String,
    val levels: List<BuddyLevel>
) 