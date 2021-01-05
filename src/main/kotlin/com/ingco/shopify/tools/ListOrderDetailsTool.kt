package com.ingco.shopify.tools

import com.ingco.shopify.api.functions.OrderDetailsFunction
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify

fun main() {
    val config = loadConfig()
    val orderDetailsFunction = OrderDetailsFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])

    orderDetailsFunction.apply("#1431")
}