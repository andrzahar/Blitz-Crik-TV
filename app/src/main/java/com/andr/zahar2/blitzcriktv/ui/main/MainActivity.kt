package com.andr.zahar2.blitzcriktv.ui.main

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Device
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andr.zahar2.blitzcriktv.ui.second.SecondActivity
import com.andr.zahar2.blitzcriktv.ui.theme.BlitzCrikTvTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var focusHost: (Boolean) -> Unit
    private lateinit var nextActivity: () -> Unit

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        event?.let {
            if (it.action == ACTION_DOWN) {
                when (it.keyCode) {
                    KEYCODE_DPAD_LEFT -> {
                        focusHost(true)
                        return true
                    }
                    KEYCODE_DPAD_RIGHT -> {
                        focusHost(false)
                        return true
                    }
                    KEYCODE_DPAD_UP, KEYCODE_DPAD_DOWN -> {
                        nextActivity()
                        return true
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val viewModel = hiltViewModel<MainActivityViewModel>()

            var host by remember { mutableStateOf(viewModel.host) }
            var port by remember { mutableStateOf(viewModel.port.toString()) }

            var hostSelected by remember { mutableStateOf(true) }

            focusHost = {
                hostSelected = it
            }

            nextActivity = {
                viewModel.onButtonClick(host, port) {
                    startActivity(Intent(this, SecondActivity::class.java))
                }
            }

            Content(
                host = host,
                port = port,
                hostSelected,
                hostChange = { host = it },
                portChange = { port = it }
            )
        }
    }
}

@Composable
private fun Content(
    host: String,
    port: String,
    hostSelected: Boolean,
    hostChange: (String) -> Unit,
    portChange: (String) -> Unit
) {
    BlitzCrikTvTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row {
                    val focusManager = LocalFocusManager.current

                    if (hostSelected) {
                        focusManager.moveFocus(FocusDirection.Left)
                    } else {
                        focusManager.moveFocus(FocusDirection.Right)
                    }

                    TextField(
                        value = host,
                        onValueChange = hostChange,
                        label = { Text("Хост") },
                        modifier = Modifier
                            .width(200.dp)
                            .focusable()
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    TextField(
                        value = port,
                        onValueChange = portChange,
                        label = { Text("Порт") },
                        modifier = Modifier.focusable()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "Сейчас выбрано поле для ввода ${if (hostSelected) "хоста" else "порта"}." +
                        "\nНажмите кнопку влево или вправо на пульте ДУ для переключения поля." +
                        "\nЧтобы перейти дальше, нажмите кнопку вверх или вниз на пульте ДУ.")
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = Devices.DESKTOP
)
@Composable
fun DefaultPreview() {
    BlitzCrikTvTheme {
        Content(
            host = "192.168.10.89",
            port = "2207",
            true,
            hostChange = { },
            portChange = { }
        )
    }
}