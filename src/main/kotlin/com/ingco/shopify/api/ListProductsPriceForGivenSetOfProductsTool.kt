package com.ingco.shopify.api

fun main() {
    val productStore = ProductStore.init()
    val handles = productStore.products().map { it.getStringValue("handle") }

    allProductCodes()
        .filter { productCode ->
            handles.any {
                it.toUpperCase().contains(productCode.replace(".", "-"))
            }
        }
        .forEach { productCode ->
            val handle = handleFor(productCode, handles)
            val data = productStore.products().first { it.getStringValue("handle").equals(handle) }!!
            val variant = data.getArrayNode("variants").get(0)

            println("$productCode, ${variant.getStringValue("price")}")
        }
}

private fun handleFor(productCode: String, handles: List<String>): String {
    return handles.filter {
        it.toUpperCase().contains(productCode.replace(".", "-"))
    }.minBy { it.length }!!
}

private fun allProductCodes(): List<String> = readLines("all-products.txt")

