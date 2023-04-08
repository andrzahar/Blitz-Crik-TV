package com.zahar2.andr.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
enum class Video {
    NONE, START, ROUND_1, ROUND_2, ROUND_3;

    override fun toString(): String = Json.encodeToString(this)
}

fun String.toVideo(): Video = Json.decodeFromString(this)