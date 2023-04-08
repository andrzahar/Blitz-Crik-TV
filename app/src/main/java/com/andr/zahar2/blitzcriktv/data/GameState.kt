package com.andr.zahar2.blitzcriktv.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
enum class GameState {
    BEFORE_START, SPLASH_SCREEN, RULES, GAME, WINNER;

    override fun toString(): String = Json.encodeToString(this)
}

fun String.toGameState(): GameState = Json.decodeFromString(this)