package com.funnco.stockwatcher.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import com.funnco.stockwatcher.R
import com.funnco.stockwatcher.activity.main.MainActivity
import com.funnco.stockwatcher.common.finn.Repository

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
        Repository.init {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}