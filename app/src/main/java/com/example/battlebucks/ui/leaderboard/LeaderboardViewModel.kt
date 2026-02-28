package com.example.battlebucks.ui.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battlebucks.engine.ScoreGenerator
import com.example.battlebucks.leaderboard.LeaderboardManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LeaderboardViewModel : ViewModel() {

    private val scoreGenerator = ScoreGenerator(playerCount = 20)

    private val leaderboardManager =
        LeaderboardManager(
            scoreGenerator = scoreGenerator,
            externalScope = viewModelScope
        )

    private val currentUserId = "0" // Player_1

    val uiState: StateFlow<LeaderboardUiState> =
        leaderboardManager.leaderboard
            .map { list ->

                val currentUser = list.find { it.playerId == currentUserId }

                LeaderboardUiState(
                    leaderboard = list,
                    currentUserRank = currentUser?.rank,
                    currentUserScore = currentUser?.score
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = LeaderboardUiState()
            )

    init {
        leaderboardManager.start()
    }

    override fun onCleared() {
        super.onCleared()
        leaderboardManager.stop()
    }
}