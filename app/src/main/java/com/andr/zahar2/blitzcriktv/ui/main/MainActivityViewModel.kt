package com.andr.zahar2.blitzcriktv.ui.main

import androidx.lifecycle.ViewModel
import com.andr.zahar2.blitzcriktv.api.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val api: Api): ViewModel() {

    val host = api.host
    val port = api.port

    fun onButtonClick(host: String, port: String, after: () -> Unit) {
        api.host = host
        api.port = port.toInt()
        after()
    }
}