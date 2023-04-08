package com.andr.zahar2.blitzcriktv.data.question

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
enum class QuestionState {
    INVISIBLE, ONLY_QUESTION, ANSWER;

    override fun toString(): String = Json.encodeToString(this)
}

fun String.toQuestionState(): QuestionState = Json.decodeFromString(this)
