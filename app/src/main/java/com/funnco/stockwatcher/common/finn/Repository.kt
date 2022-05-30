package com.funnco.stockwatcher.common.finn

import android.app.Activity
import android.util.Log
import com.funnco.stockwatcher.activity.main.StockUpdateInterface
import com.funnco.stockwatcher.common.model.StockModel
import com.funnco.stockwatcher.common.model.StockUpdateModel
import com.google.gson.Gson
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.StockSymbol
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*

/**
 * This is repository class which is responsible for subscribing to news about stocks and listening
 * to these news.
 *
 * @author FunnCo
 */
class Repository {
    companion object {

        // Tag for logging purposes
        private val TAG = "ClientWebSocket"

        // List to contain all stocks
        lateinit var listOfStocks: List<StockModel>

        // Websocket connection session
        lateinit var wssSession: DefaultClientWebSocketSession

        // Map to hold all current subscribed stocks
        val mapOfSubscribers: MutableMap<String, StockUpdateInterface> = mutableMapOf()

        /**
         * This method is used to connect to the server and start listening to it's messages
         *
         * @author FunnCo
         * @param activity activity from where this method is being called
         * @param callback callback for activity that server connection is set
         */
        fun init(activity: Activity, callback: (Boolean) -> Unit) {
            ApiClient.apiKey["token"] = "c8rg8tqad3i8tv0k6i60"
            val apiClient = DefaultApi()

            // Coroutine is used to prevent blocking the UI thread
            MainScope().launch(context = Dispatchers.IO) {

                // Getting symbols of all companies on the US exchange market
                val symbols = apiClient.stockSymbols("US", "", "", "")

                // Selecting random 50 stocks to be shown to user
                val tempList = mutableListOf<StockModel>()
                for (i in 0 until 50) {
                    tempList.add(StockModel(symbols[i], apiClient.quote(symbols[i].symbol!!)))

                    // Delay between api calls to prevent exceeding limit of calls
                    delay(100)
                }
                listOfStocks = tempList

                // Websocket initialisation
                val client = HttpClient(CIO) {
                    install(WebSockets)
                }
                try {
                    client.wss(urlString = "wss://ws.finnhub.io?token=c8rg8tqad3i8tv0k6i60") {
                        wssSession = this

                        // Start callback in Activity which called init method in this class
                        callback(true)

                        // Starting to listen for all incoming messages
                        while (true) {
                            val othersMessage = incoming.receive() as? Frame.Text ?: continue
                            Log.d(TAG, "Message received: ${othersMessage.readText()}")
                            activity.runOnUiThread {
                                interpretMessage(othersMessage.readText())
                            }
                        }
                    }
                }catch (e: Exception){
                    Log.d("TAAAAG", e.stackTraceToString())
                }
            }
        }

        /**
         * Method to interpret received message from the server
         */
        private fun interpretMessage(message: String) {
            // Parsing message into StockUpdateModel object.
            // If message is not about the trade, it will be ignored
            val update = Gson().fromJson(message, StockUpdateModel::class.java)
            if (update.data != null && update.data[update.data.lastIndex].p != null) {
                mapOfSubscribers[update.data[update.data.lastIndex].s]?.updateCertainStock(update.data[update.data.lastIndex].p!!)
            }
        }


        fun subscribeToUpdates(subscriber: StockUpdateInterface, symbol: StockSymbol) {
            MainScope().launch(Dispatchers.IO) {
                var subscriptionComplete = false
                while (!subscriptionComplete) {
                    mapOfSubscribers.put(symbol.symbol!!, subscriber)
                    val myMessage = "{\"type\":\"subscribe\",\"symbol\":\"${symbol.symbol}\"}"
                    Log.d(TAG, "Sending message")
                    wssSession.send(myMessage)
                    delay(200)
                    subscriptionComplete = true
                }
            }
        }
    }
}

