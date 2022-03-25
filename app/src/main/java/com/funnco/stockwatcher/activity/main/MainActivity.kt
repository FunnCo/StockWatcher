package com.funnco.stockwatcher.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.funnco.stockwatcher.R
import com.funnco.stockwatcher.common.finn.Repository
import com.funnco.stockwatcher.common.model.StockModel
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


    lateinit var updateInterface: StockUpdateInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateInterface = object : StockUpdateInterface {
            override fun updateCertainStock(stock: StockModel) {
                Log.d("UpateStocks", "Updated 1")
            }

            override fun updateAllStocks(stocks: List<StockModel>) {
                Log.d("UpateStocks", "Updated 1")
            }

            override fun updateTest(value: Double) {
                supportActionBar?.title = value.toString()
            }
        }

        Repository.subscribeToUpdates(updateInterface, Repository.listOfStocks[0].symbol)
        binding.recycler.adapter = RecyclerAdapter(Repository.listOfStocks)



    }
}