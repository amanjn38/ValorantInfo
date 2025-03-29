package com.example.valorantinfo.data.models.gamemodes

import com.google.gson.annotations.SerializedName

data class GameMode(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("displayIcon")
    val displayIcon: String?,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("isMatchmakingEnabled")
    val isMatchmakingEnabled: Boolean,
    @SerializedName("isTeamVoiceEnabled")
    val isTeamVoiceEnabled: Boolean,
    @SerializedName("minimapVisibility")
    val minimapVisibility: Boolean,
    @SerializedName("orbCount")
    val orbCount: Int,
    @SerializedName("teamRoles")
    val teamRoles: List<String>?
)

data class GameFeatureOverride(
    @SerializedName("featureName")
    val featureName: String,
    @SerializedName("state")
    val state: Boolean
)

data class GameRuleBoolOverride(
    @SerializedName("ruleName")
    val ruleName: String,
    @SerializedName("state")
    val state: Boolean
) 