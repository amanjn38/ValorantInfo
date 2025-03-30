package com.example.valorantinfo.data.models.maps

data class Data(
    val assetPath: String,
    val callouts: List<Callout>,
    val coordinates: String,
    val displayIcon: String,
    val displayName: String,
    val listViewIcon: String,
    val listViewIconTall: String,
    val mapUrl: String,
    val narrativeDescription: Any,
    val premierBackgroundImage: String,
    val splash: String,
    val stylizedBackgroundImage: String,
    val tacticalDescription: String,
    val uuid: String,
    val xMultiplier: Double,
    val xScalarToAdd: Double,
    val yMultiplier: Double,
    val yScalarToAdd: Double
)