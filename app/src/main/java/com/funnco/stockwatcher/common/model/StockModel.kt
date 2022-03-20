package com.funnco.stockwatcher.common.model

import io.finnhub.api.models.Quote
import io.finnhub.api.models.StockSymbol

class StockModel(
    val symbol: StockSymbol,
    val quote: Quote
)