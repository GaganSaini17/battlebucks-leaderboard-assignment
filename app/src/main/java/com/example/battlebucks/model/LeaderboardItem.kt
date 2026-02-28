package com.example.battlebucks.model

data class LeaderboardItem(
    val playerId: String,
    val username: String,
    val score: Int,
    val rank: Int,
    val isUpdated: Boolean = false,
)