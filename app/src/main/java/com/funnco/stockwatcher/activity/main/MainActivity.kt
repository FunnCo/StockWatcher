package com.funnco.stockwatcher.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.funnco.stockwatcher.R
import com.funnco.stockwatcher.common.finn.Repository
import com.funnco.stockwatcher.databinding.ActivityMainBinding
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.adapter = RecyclerAdapter(Repository.listOfStocks)


//        val client = HttpClient(CIO) {
//            install(WebSockets)
//        }
//        MainScope().launch {
//            val response =
//                client.webSocket("wss://ws.finnhub.io?token=${resources.getString(R.string.finn_api_key)}") {
//
//                    while (true) {
//                        var messages = incoming.receive() as? Frame.Text
//                        Log.d("ClientWebSocket", messages!!.readText())
//                        if (messages.readText() == "{\"type\":\"ping\"}") {
//
//
//                        }
//                    }
//
//                }
//        }
    }
}