package com.andr.zahar2.blitzcriktv.ui.second

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.andr.zahar2.blitzcriktv.R
import com.andr.zahar2.blitzcriktv.data.GameState
import com.andr.zahar2.blitzcriktv.data.question.Question
import com.andr.zahar2.blitzcriktv.data.question.QuestionState
import com.andr.zahar2.blitzcriktv.ui.theme.BlitzCrikTvTheme
import com.zahar2.andr.data.Video
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {

            val viewModel = hiltViewModel<SecondActivityViewModel>()

            val gameState = viewModel.stateGameState.value
            val users = viewModel.stateUsersScore.value
            val question = viewModel.stateQuestion.value
            val video = viewModel.stateVideo.value

            if (video != Video.NONE) {
                MyVideo(video, viewModel::onVideoEnd)
            } else {
                when (gameState) {
                    GameState.BEFORE_START -> BeforeStart()
                    GameState.SPLASH_SCREEN -> SplashScreen()
                    GameState.RULES -> Rules()
                    GameState.GAME -> Game(usersScore = users, question = question)
                    GameState.WINNER -> {}
                }
            }
        }
    }
}

@Composable
private fun MyText(
    text: String,
    fontSize: TextUnit,
    color: Color = Color.White,
    modifier: Modifier = Modifier
) = Text(
    text = text,
    color = color,
    fontSize = fontSize,
    fontWeight = FontWeight.Bold,
    style = TextStyle(
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(5.0f, 10.0f),
            blurRadius = 75f
        )
    ),
    textAlign = TextAlign.Center,
    fontFamily = FontFamily(Font(R.font.istok_web_bold)),
    modifier = modifier.fillMaxWidth()
)

@Composable
private fun BeforeStart() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {}
}

@Composable
private fun SplashScreen() {
    Image(
        painter = painterResource(R.drawable.splash),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun Rules() {
    Image(
        painter = painterResource(R.drawable.splash_second),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
    MyText(
        text = "\nПРАВИЛА\nИГРЫ",
        fontSize = 100.sp,
        modifier = Modifier
            .fillMaxHeight()
    )

}

@Composable
private fun Game(
    usersScore: Map<String, String>,
    question: Question
) {
    BlitzCrikTvTheme(darkTheme = true, dynamicColor = false) {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(86, 81, 115)
        ) {

            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp)
                ) {

                    for (user in usersScore) {
                        Column(modifier = Modifier.weight(1f)) {
                            MyText(
                                text = user.key.uppercase(),
                                fontSize = 35.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            MyText(
                                text = user.value,
                                fontSize = 60.sp
                            )
                        }
                    }
                }

                if (question.questionState != QuestionState.INVISIBLE && question.author.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    MyText(
                        text = question.author.uppercase(),
                        fontSize = 25.sp,
                        color = Color(0xFF98FB98)
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                if (question.questionState != QuestionState.INVISIBLE) {
                    Spacer(modifier = Modifier.height(70.dp))
                    MyText(
                        text = question.question.uppercase(),
                        fontSize = 25.sp
                    )
                }
            }

            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                if (question.questionState == QuestionState.ANSWER) {
                    MyText(
                        text = question.answer.uppercase(),
                        fontSize = 30.sp,
                        color = Color(204, 84, 73)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }

                if (question.total != 0)
                    LinearProgressIndicator(
                        progress = question.number.toFloat() / question.total.toFloat(),
                        modifier = Modifier.fillMaxWidth(),
                        trackColor = Color.Transparent
                    )
            }
        }
    }
}

private fun getVideoUri(
    context: Context,
    video: Video
): Uri {
    val name = when (video) {
        Video.NONE -> ""
        Video.START -> R.raw.start_video
        Video.ROUND_1 -> R.raw.round1
        Video.ROUND_2 -> R.raw.round2
        Video.ROUND_3 -> R.raw.round3
    }
    return Uri.parse(
        "android.resource://" + context.packageName.toString() + "/" +
                name
    )
}

@Composable
private fun MyVideo(
    video: Video,
    onVideoEnd: () -> Unit
) {

    val context = LocalContext.current
    val playerView = remember {
        val layout = LayoutInflater.from(context).inflate(R.layout.video_view, null, false)
        val videoView = layout.findViewById(R.id.view_video) as VideoView
        videoView.apply {
            setVideoURI(getVideoUri(context, video))
            setOnCompletionListener {
                onVideoEnd()
            }
            start()
        }
    }

    AndroidView({ playerView })
}

@Preview(
    showBackground = true,
    device = Devices.DESKTOP
)
@Composable
fun BeforeStartPreview() {
    BeforeStart()
}

@Preview(
    showBackground = true,
    device = Devices.DESKTOP
)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}

@Preview(
    showBackground = true,
    device = Devices.DESKTOP
)
@Composable
fun RulesPreview() {
    Rules()
}

@Preview(
    showBackground = true,
    device = Devices.DESKTOP
)
@Composable
fun GamePreview() {
    Game(
        usersScore = mapOf(
            "Матвей" to "1",
            "Полина" to "2",
            "Анастейша" to "0",
            "Диана" to "4"
        ),
        question = Question(
            "Джордж Гордон Байрон",
            "Всем известно, что древнегреческие статуи изображали усредненные, но прекрасные версии персонажей или реальных личностей. Эти фигуры являлись идеальными в понимании греков. Одна из античных статуй Венеры носит имя Каллипига, что переводится буквально как…",
            "сойдет с ума и ужалит себя до \nсмерти",
            "",
            QuestionState.ANSWER,
            "",
            2,
            10
        )
    )
}

@Preview(
    showBackground = true,
    device = Devices.DESKTOP
)
@Composable
fun VideoPreview() {
    MyVideo(
        Video.START
    ) { }
}