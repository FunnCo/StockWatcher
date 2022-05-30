package com.funnco.stockwatcher.common.model

import io.finnhub.api.models.Quote
import io.finnhub.api.models.StockSymbol

/**
 * This class is a model of Stock. Objects of this class are used for initialisation of visible stocks
 *
 * @author FunnCo
 * @param symbol symbol of company which owns the stock
 * @param quote quote of the company
 */

class StockModel(
    val symbol: StockSymbol,
    val quote: Quote
)