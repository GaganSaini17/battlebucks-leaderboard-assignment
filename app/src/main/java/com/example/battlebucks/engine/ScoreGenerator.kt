package com.example.battlebucks.engine

import com.example.battlebucks.model.Player
import com.example.battlebucks.model.ScoreUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random

class ScoreGenerator(
    playerCount: Int = 20
) {

    private val players: List<Player> = List(playerCount) { index ->
        Player(
            id = index.toString(),
            username = "Player_${index + 1}"
        )
    }

    private val scoreMap: MutableMap<String, Int> =
        players.associate { it.id to 0 }.toMutableMap()

    fun getPlayers(): List<Player> = players

    fun startGeneratingScores(): Flow<ScoreUpdate> = flow {

        val random = Random(System.currentTimeMillis())

        while (true) {

            // Random delay between 500ms to 2000ms
            val delayTime = random.nextLong(500, 2000)
            delay(delayTime)

            // Pick random player
            val randomPlayer = players[random.nextInt(players.size)]

            // Deterministic incremental increase (1–20)
            val increment = random.nextInt(1, 21)

            val newScore = scoreMap.getValue(randomPlayer.id) + increment
            scoreMap[randomPlayer.id] = newScore

            emit(
                ScoreUpdate(
                    playerId = randomPlayer.id,
                    newScore = newScore
                )
            )
        }
    }.flowOn(Dispatchers.Default)
}