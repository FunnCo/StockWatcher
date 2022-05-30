package com.funnco.stockwatcher.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
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
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initRepo()
        } catch(exception: ClosedReceiveChannelException) {
            AlertDialog.Builder(this).setTitle("Ошибка").setMessage("Ошибка подключения. Пожалуйста проверьте подключение, и попробуйте включить приложение снова").create().show()
            System.exit(2);
        }
    }

    fun initRepo(){
        binding.recycler.adapter = RecyclerAdapter(Repository.listOfStocks)
    }
}