package com.andr.zahar2.blitzcriktv.api

import com.andr.zahar2.blitzcriktv.data.GameState
import com.andr.zahar2.blitzcriktv.data.UserScore
import com.andr.zahar2.blitzcriktv.data.question.Question
import com.andr.zahar2.blitzcriktv.data.question.toQuestion
import com.andr.zahar2.blitzcriktv.data.toGameState
import com.andr.zahar2.blitzcriktv.data.toUserScore
import com.zahar2.andr.data.Video
import com.zahar2.andr.data.toVideo
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Api(private val client: HttpClient) {

    var host = "192.168.10.89"
    var port = 2207
    private val userPath = "/user"
    private val gamePath = "/state"
    private val questionPath = "/question"
    private val video = "/video"

    private fun <T> baseWebSocket(path: String, toType: (String) -> T): Flow<T> = flow {
        client.webSocket(host = host, port = port, path = path) {
            incoming.consumeEach { frame ->
                if (frame !is Frame.Text) return@consumeEach
                val data = toType(frame.readText())
                emit(data)
            }
        }
    }

    fun userScoreListener(): Flow<UserScore> = baseWebSocket(userPath) { it.toUserScore() }

    fun gameStateListener(): Flow<GameState> = baseWebSocket(gamePath) { it.toGameState() }

    fun questionListener(): Flow<Question> =
        baseWebSocket(questionPath) { it.toQuestion() }

    private lateinit var socket: WebSocketSession

    fun videoListener(): Flow<Video> = flow {
        socket = client.webSocketSession(host = host, port = port, path = video)
        socket.incoming.consumeEach { frame ->
            if (frame !is Frame.Text) return@consumeEach
            val video = frame.readText().toVideo()
            emit(video)
        }
    }

    fun sendVideo(video: Video): Flow<Any> = flow {
        socket.send(video.toString())
    }
}