package com.ingco.shopify.api

import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify

fun main() {
    val config = loadConfig()
    val setProductPriceFunction = SetProductPriceFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])

    val productStore = ProductStore.init()
    val handles = productStore.products().map { it.getStringValue("handle") }

    allProductCodesToPrices()
        .filter { (productCode, price, quantity) ->
            handles.any {
                it.toUpperCase().contains(productCode.replace(".", "-"))
            }
        }
        .map { (productCode, price, quantity) ->
            val handle = handleFor(productCode, handles)
            val variantNode =
                productStore.products().first { it.getStringValue("handle").equals(handle) }!!.getArrayNode("variants")
                    .get(0)
            Product(
                productCode,
                variantNode.getNumberValue("id"),
                price,
                variantNode.getNumberValue("inventory_item_id"),
                quantity
            )
        }.forEach { (productCode, id, price, inventoryItemId, quantity) ->
            setProductPriceFunction.apply(productCode, id, price)
            //updateProductInventoryFunction.apply(inventoryItemId, quantity)
        }
}

data class Product(
    val productCode: String,
    val variantId: String,
    val price: String,
    val inventoryItemId: String,
    val quantity: String
)

private fun handleFor(productCode: String, handles: List<String>): String {
    val handle = handles.filter {
        it.toUpperCase().contains(productCode.replace(".", "-"))
    }.minBy { it.length }!!
    return handle
}

fun allProductCodesToPrices(): Set<Triple<String, String, String>> =
    readLines("all-products.txt")
        .map {
            val array = it.split(",")
            Triple(array[0], array[1].trim(), "0")
        }.toSet()