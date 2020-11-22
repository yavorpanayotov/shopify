package com.ingco.shopify.api

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