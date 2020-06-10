package com.ingco.shopify.api

import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify

fun main() {
    val productStore = ProductStore.init()

    val config = loadConfig()
    val addTagsProductFunction = AddTagsProductFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])

    productStore.products()
        .filter { it.isStringValue("handle") }
        .filter { industrialProductCodes().any { code -> it.getStringValue("handle").toUpperCase().contains("-$code") } }
        .filter { !it.getStringValue("tags").contains("индустриален") }
        .forEach { addTagsProductFunction.apply("индустриален", it) }
}

fun industrialProductCodes(): List<String> = readLines("industrial-products.txt")