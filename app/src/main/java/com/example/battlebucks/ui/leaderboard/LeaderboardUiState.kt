package com.example.battlebucks.ui.leaderboard

import com.example.battlebucks.model.LeaderboardItem

data class LeaderboardUiState(
    val leaderboard: List<LeaderboardItem> = emptyList(),
    val currentUserRank: Int? = null,
    val currentUserScore: Int? = null
)