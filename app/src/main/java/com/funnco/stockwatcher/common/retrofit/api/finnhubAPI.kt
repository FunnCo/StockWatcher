package com.funnco.stockwatcher.common.retrofit.api

import retrofit2.http.GET

interface finnhubAPI {

    @GET("/stock/symbol")
    fun getStocks()
}