package com.andr.zahar2.blitzcriktv.ui.second

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andr.zahar2.blitzcriktv.api.Api
import com.andr.zahar2.blitzcriktv.data.GameState
import com.andr.zahar2.blitzcriktv.data.UserScore
import com.andr.zahar2.blitzcriktv.data.question.Question
import com.zahar2.andr.data.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SecondActivityViewModel @Inject constructor(private val api: Api): ViewModel() {

    private val _stateGameState = mutableStateOf(GameState.BEFORE_START)
    val stateGameState: State<GameState> = _stateGameState

    private val _stateUsersScore = mutableStateOf(mapOf<String, String>())
    val stateUsersScore: State<Map<String, String>> = _stateUsersScore

    private val _stateQuestion = mutableStateOf(Question.emptyQuestion())
    val stateQuestion: State<Question> = _stateQuestion

    private val _stateVideo = mutableStateOf(Video.NONE)
    val stateVideo: State<Video> = _stateVideo

    init {
        api.userScoreListener().onEach {
            val old = _stateUsersScore.value.toMutableMap()
            val points = pointsToString(it.points?: 0f)
            old[it.name] = points
            _stateUsersScore.value = old
        }.launchIn(viewModelScope)

        api.gameStateListener().onEach {
            _stateGameState.value = it
        }.launchIn(viewModelScope)

        api.questionListener().onEach {
            _stateQuestion.value = it
        }.launchIn(viewModelScope)

        api.videoListener().onEach {
            _stateVideo.value = it
        }.launchIn(viewModelScope)
    }

    fun onVideoEnd() {
        _stateVideo.value = Video.NONE
        api.sendVideo(Video.NONE).launchIn(viewModelScope)
    }

    private fun pointsToString(points: Float): String {
        val d2 = points % 1
        val res = if (d2 == 0f) {
            (points - d2).toInt().toString()
        } else {
            points.toString().replace('.', ',')
        }
        return res
    }
}