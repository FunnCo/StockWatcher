package com.funnco.stockwatcher.common.finn

import android.util.Log
import com.funnco.stockwatcher.common.model.StockModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import io.finnhub.api.models.Quote
import io.finnhub.api.models.StockSymbol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Repository {
    companion object {

        lateinit var listOfStocks: List<StockModel>

        fun init() {
            ApiClient.apiKey["token"] = "c8rg8tqad3i8tv0k6i60"
            val apiClient = DefaultApi()


            MainScope().launch(context = Dispatchers.IO){
                val symbols = apiClient.stockSymbols("US", "", "", "")

                val tempList = mutableListOf<StockModel>()
                for (i in 0..20) {
                    tempList.add(StockModel(symbols[i], apiClient.quote(symbols[i].symbol!!)))
                    Log.d("Test", "will wait now")
                    delay(100)
                }
                Log.d("Test", "yay")
                listOfStocks = tempList

            }
        }

        fun getStockModels() : List<StockModel> {
            return listOfStocks
        }

    }
}