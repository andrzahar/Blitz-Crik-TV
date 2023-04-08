package com.andr.zahar2.blitzcriktv.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class UserScore(
    val name: String,
    var points: Float?
) {
    override fun toString(): String = Json.encodeToString(this)
}

fun String.toUserScore(): UserScore = Json.decodeFromString(this)
