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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Repository {
    companion object {

        lateinit var listOfStocks: List<StockModel>

        fun init(callback: (Boolean) -> Unit) {
            ApiClient.apiKey["token"] = "c8rg8tqad3i8tv0k6i60"
            val apiClient = DefaultApi()


            MainScope().launch(context = Dispatchers.IO) {
                val symbols = apiClient.stockSymbols("US", "", "", "")

                val tempList = mutableListOf<StockModel>()
                for (i in 0..5) {
                    tempList.add(StockModel(symbols[i], apiClient.quote(symbols[i].symbol!!)))
                    Log.d("Test", "will wait now")
                    delay(100)
                }
                Log.d("Test", "yay")
                listOfStocks = tempList
                callback(true)
            }
        }

        fun getStockModels(): List<StockModel> {
            return listOfStocks
        }

        fun subscribeToUpdates(subscriber: StockUpdateInterface, symbol: StockSymbol) {
            val client = HttpClient(CIO) {
                install(WebSockets)
            }
            MainScope().launch {
                val response =
                    client.webSocket("wss://ws.finnhub.io?token=c8rg8tqad3i8tv0k6i60") {
                        while (true) {
                            var messages = incoming.receive() as? Frame.Text
                            Log.d("ClientWebSocket", messages!!.readText())
                            if (messages.readText() == "{\"type\":\"ping\"}") {
                                outgoing.send(Frame.Text("{\"type\":\"subscribe\",\"symbol\":\"${symbol.symbol}\"}"))
                                Log.d("ClientWebSocket", "Sent query with symbol: ${symbol.symbol}")
                            }

                            // {"data":[{"p":7296.89,"s":"BINANCE:BTCUSDT","t":1575526691134,"v":0.011467}],"type":"trade"}
                            if (messages.readText().contains("{\"data\":")) {
                                val a = Klaxon().parse<StockUpdateModel>(messages.readText())
                                Log.d("QWERTY", "it parsed? ${a?.type}")
                                subscriber.updateTest(a?.data?.get(0)?.p!!)
                            }
                        }

                    }
            }
        }

    }
}