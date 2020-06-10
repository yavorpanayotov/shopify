package com.ingco.shopify.api

fun main() {
    val handles = allHandlesInShopify()

    allProducts()
        .filter { product -> handles.none { it.toUpperCase().contains(product) } }
        .forEach { println(it) }
}

fun allHandlesInShopify(): List<String> {
    return ProductStore.init().products().map { it.getStringValue("handle") }
}

fun allProducts() = readLines("all-products.txt")