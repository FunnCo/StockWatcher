package com.funnco.stockwatcher.common.model

/**
 * This is a model of a response from server which represents a trade deals. It contains data
 * about company, about last price and type of deal.
 *
 * @author FunnCo
 * @param data information about last deals.
 * @param type type of deals in parameter "data"
 */

class StockUpdateModel(
    val data : List<QuoteModel>,
    val type: String
)