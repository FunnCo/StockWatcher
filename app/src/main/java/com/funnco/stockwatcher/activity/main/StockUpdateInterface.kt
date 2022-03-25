package com.funnco.stockwatcher.activity.main

import com.funnco.stockwatcher.common.model.StockModel

interface StockUpdateInterface {
    fun updateCertainStock(stock: StockModel)

    fun updateAllStocks(stocks: List<StockModel>)

    fun updateTest(value: Double)
}