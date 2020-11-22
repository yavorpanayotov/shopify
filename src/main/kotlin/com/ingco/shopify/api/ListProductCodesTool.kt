package com.ingco.shopify.api

fun main() {
    val productStore = ProductStore.init()

    productStore.products()
        .map { it.getStringValue("handle") }
        .map { productCode(it) }
        .forEach {println(it) }
}