package com.funnco.stockwatcher.common.model

class StockUpdateModel(
    val data : List<QuoteModel>,
    val type: String
)


// {"data":[{"p":7296.89,"s":"BINANCE:BTCUSDT","t":1575526691134,"v":0.011467}],"type":"trade"}