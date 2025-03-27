package com.example.valorantinfo.data.models.agentDetails

data class VoiceLine(
    val voiceLineLocalization: VoiceLineLocalization?,
    val minDuration: Double,
    val maxDuration: Double,
    val mediaList: List<Media>,
)

data class VoiceLineLocalization(
    val regionName: String,
    val wwise: String,
    val wave: String,
)

data class Media(
    val id: Int,
    val wwise: String,
    val wave: String,
)
