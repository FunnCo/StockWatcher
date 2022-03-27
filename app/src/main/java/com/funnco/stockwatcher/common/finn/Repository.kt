package com.funnco.stockwatcher.common.finn

import android.util.Log
import com.beust.klaxon.Klaxon
import com.funnco.stockwatcher.activity.main.StockUpdateInterface
import com.funnco.stockwatcher.common.model.StockModel
import com.funnco.stockwatcher.common.model.StockUpdateModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.Quote
import io.finnhub.api.models.StockSymbol
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import okhttp3.internal.notify
import okhttp3.internal.wait

class Repository {
    companion object {

        lateinit var listOfStocks: List<StockModel>
        val receivedMessages = mutableListOf<String>()

        fun init(callback: (Boolean) -> Unit) {
            ApiClient.apiKey["token"] = "c8rg8tqad3i8tv0k6i60"
            val apiClient = DefaultApi()


            MainScope().launch(context = Dispatchers.IO) {
                val symbols = apiClient.stockSymbols("US", "", "", "")

                val tempList = mutableListOf<StockModel>()
                for (i in 0..1) {
                    tempList.add(StockModel(symbols[i], apiClient.quote(symbols[i].symbol!!)))
                    Log.d("Test", "will wait now")
                    delay(100)
                }
                Log.d("Test", "yay")
                listOfStocks = tempList

                incomingJob = MainScope().launch {
                    while (true) {

                    }
                }

                // Инициализация вебсокета
                val client = HttpClient(CIO) {
                    install(WebSockets)
                }
                client.webSocket("wss://ws.finnhub.io?token=c8rg8tqad3i8tv0k6i60") {
                    webSocket = this
                }

                callback(true)
            }
        }

        lateinit var webSocket: DefaultClientWebSocketSession
        lateinit var incomingJob: Job
        lateinit var outgoingJob: Job

        suspend fun subscribeToUpdates(subscriber: StockUpdateInterface, symbol: StockSymbol) {
            // Поток для отправки подписок на событие
            MainScope().launch {
                Log.d("SubscriptionManager", "Subscribed to ${symbol.symbol}")
                if (outgoingJob != null) {
                    outgoingJob.join()
                }
                outgoingJob = MainScope().launch {
                    webSocket.send(Frame.Text("{\"type\":\"subscribe\",\"symbol\":\"${symbol.symbol}\"}"))
                    Log.d(
                        "ClientWebSocket",
                        "Sent query with symbol: ${symbol.symbol}"
                    )
                }

            }

            // Поток на принятие результатов подписки
            MainScope().launch {
                Log.d("SubscriptionManager", "Listening to ${symbol.symbol}")
                if (incomingJob != null){
                    incomingJob.join()
                }
                incomingJob = MainScope().launch {
                    val message = webSocket.incoming.receive() as Frame.Text
                    receivedMessages.add(message.readText())
                }
            }
        }
    }
}
