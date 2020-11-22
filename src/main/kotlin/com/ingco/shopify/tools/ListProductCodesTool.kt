package com.ingco.shopify.tools

import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.productCode

fun main() {
    val productStore = ProductStore.init()

    productStore.products()
        .map { it.getStringValue("handle") }
        .map { productCode(it) }
        .forEach {println(it) }
}