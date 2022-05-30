package com.funnco.stockwatcher.common.model

/**
 * This is a quote model. It contains information about current price and symbol of the company
 * Other info from quote does not matter for this application
 *
 * @author FunnCo
 * @param p current price
 * @param s symbol of the company
 */

class QuoteModel(
    val c: Any?,
    val p: Double?,
    val s: String?,
    val t: Any?,
    val v: Any?
)

//{"data":[{"p":7296.89,"s":"BINANCE:BTCUSDT","t":1575526691134,"v":0.011467}],"type":"trade"}