package com.funnco.stockwatcher.activity.main

/**
 * Interface for updating stock prices in UI
 * @author FunnCo
 */

interface StockUpdateInterface {

    /**
     * Update callback for item in RecyclerView. It is called when stock changes the price
     * @param price current price of the stock
     */
    fun updateCertainStock(price: Double)
}