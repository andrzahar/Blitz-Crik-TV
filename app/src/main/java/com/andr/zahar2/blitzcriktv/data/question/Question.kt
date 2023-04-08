package com.andr.zahar2.blitzcriktv.data.question

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Question(
    val author: String,
    val question: String,
    val answer: String,
    val comment: String,
    val questionState: QuestionState,
    val roundName: String,
    val number: Int,
    val total: Int
) {

    companion object {
        fun emptyQuestion(): Question =
            Question("", "", "", "", QuestionState.INVISIBLE, "", 0, 0)
    }

    override fun toString(): String = Json.encodeToString(this)
}

fun String.toQuestion(): Question = Json.decodeFromString(this)
