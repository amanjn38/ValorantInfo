package com.example.valorantinfo.data.models.contracts

enum class RewardType {
    XP,
    VP,
    SKIN,
    SPRAY,
    TITLE,
    BUDDY,
    ;

    companion object {
        fun fromString(value: String): RewardType {
            return when (value.lowercase()) {
                "currency" -> if (value.contains("vp", ignoreCase = true)) VP else XP
                "spray" -> SPRAY
                "title" -> TITLE
                "gunbuddy" -> BUDDY
                "character" -> SKIN
                else -> SKIN
            }
        }
    }
}
