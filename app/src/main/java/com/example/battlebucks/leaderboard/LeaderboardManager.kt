package com.example.battlebucks.leaderboard

import com.example.battlebucks.engine.ScoreGenerator
import com.example.battlebucks.model.LeaderboardItem
import com.example.battlebucks.model.Player
import com.example.battlebucks.model.ScoreUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardManager(
    private val scoreGenerator: ScoreGenerator,
    private val externalScope: CoroutineScope
) {

    private val players: List<Player> = scoreGenerator.getPlayers()

    private val playerMap: Map<String, Player> =
        players.associateBy { it.id }

    private val scoreMap: MutableMap<String, Int> =
        players.associate { it.id to 0 }.toMutableMap()

    private val _leaderboard =
        MutableStateFlow<List<LeaderboardItem>>(emptyList())

    val leaderboard: StateFlow<List<LeaderboardItem>> =
        _leaderboard.asStateFlow()

    private var job: Job? = null

    fun start() {
        if (job != null) return

        job = externalScope.launch {
            scoreGenerator
                .startGeneratingScores()
                .collect { update ->
                    handleScoreUpdate(update)
                }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    private fun handleScoreUpdate(update: ScoreUpdate) {
        scoreMap[update.playerId] = update.newScore
        recomputeLeaderboard(updatedPlayerId = update.playerId)
    }

    private fun recomputeLeaderboard(updatedPlayerId: String? = null) {

        val sorted = scoreMap
            .toList()
            .sortedByDescending { (_, score) -> score }

        val result = mutableListOf<LeaderboardItem>()

        var currentRank = 0
        var previousScore: Int? = null
        var index = 0

        for ((playerId, score) in sorted) {
            index++

            if (previousScore == null || score < previousScore) {
                currentRank = index
            }

            val player = playerMap[playerId]!!

            result.add(
                LeaderboardItem(
                    playerId = player.id,
                    username = player.username,
                    score = score,
                    rank = currentRank,
                    isUpdated = playerId == updatedPlayerId
                )
            )

            previousScore = score
        }

        _leaderboard.value = result
    }
}