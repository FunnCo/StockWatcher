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
                callback(true)
            }
        }


        var isWssActive = false
        lateinit var webSocket: DefaultClientWebSocketSession
        val lock: Any = 1

        @OptIn(ExperimentalCoroutinesApi::class)
        suspend fun subscribeToUpdates(subscriber: StockUpdateInterface, symbol: StockSymbol) {
            val client = HttpClient(CIO) {
                install(WebSockets)
            }
            Log.d("SubscriptionManager", "Subscribed to ${symbol.symbol}")
            if (!isWssActive) {
//                isWssActive = true
                MainScope().launch {
                    val response =
                        client.webSocket("wss://ws.finnhub.io?token=c8rg8tqad3i8tv0k6i60") {
                            webSocket = this
                            while (true) {
                                Log.d("ClientWebSocket", "1 ${incoming.isClosedForReceive}")
                                if (incoming.isClosedForReceive) {
                                    synchronized(lock) {

                                        while (incoming.isClosedForReceive) {
                                            lock.wait()
                                        }
                                        Log.d("ClientWebSocket", "1.2")
                                    }
                                }
                                Log.d("ClientWebSocket", "1.1")
                                var messages = incoming.receive() as? Frame.Text
                                Log.d("ClientWebSocket", "3")
                                Log.d("ClientWebSocket", messages!!.readText())
                                synchronized(lock) {
                                    lock.notify()
                                }
                                outgoing.send(Frame.Text("{\"type\":\"subscribe\",\"symbol\":\"${symbol.symbol}\"}"))
                                Log.d(
                                    "ClientWebSocket",
                                    "Sent query with symbol: ${symbol.symbol}"
                                )


                                if (messages!!.readText().contains("{\"data\":")) {
                                    val a =
                                        Klaxon().parse<StockUpdateModel>(messages.readText())
                                    Log.d("QWERTY", "it parsed? ${a?.type}")
                                    subscriber.updateTest(a?.data?.get(0)?.p!!)
                                }
                            }

                        }
                }
            }
        }

    }
}
