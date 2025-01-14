package com.example.valorantinfo.data.models

data class Agent(
    val uuid: String,
    val displayName: String,
    val description: String,
    val developerName: String,
    val releaseDate: String?,
    val characterTags: List<String>?,
    val displayIcon: String,
    val displayIconSmall: String,
    val bustPortrait: String?,
    val fullPortrait: String?,
    val killfeedPortrait: String?,
    val background: String?,
    val backgroundGradientColors: List<String>?,
    val assetPath: String,
    val isFullPortraitRightFacing: Boolean,
    val isPlayableCharacter: Boolean,
    val isAvailableForTest: Boolean,
    val isBaseContent: Boolean,
    val role: Role?,
    val abilities: List<Ability>?,
    val voiceLine: VoiceLine?
)