package com.ingco.shopify.tools

import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.productCode

fun main() {
    val productStore = ProductStore.init()

    productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .forEach { (productCode, data) ->
            val variant = data.getArrayNode("variants").get(0)

            println("$productCode, ${variant.getStringValue("price")}")
        }
}