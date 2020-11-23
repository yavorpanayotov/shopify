package com.ingco.shopify.tools

import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.functions.SetProductPriceFunction
import com.ingco.shopify.api.functions.UpdateProductInventoryFunction
import com.ingco.shopify.api.productCode
import com.ingco.shopify.api.readLines
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify

fun main() {
    val config = loadConfig()
    val setProductPriceFunction = SetProductPriceFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])
    val updateProductInventoryFunction = UpdateProductInventoryFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])

    val productStore = ProductStore.init()

    val productCodeToData = productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }

    allProductCodesToPrices()
        .map { (productCode, newPrice, newQuantity) ->
            val data = productCodeToData.firstOrNull { it.first == productCode }?.second
                ?: throw IllegalArgumentException("$productCode not found")

            val variantNode = data.getArrayNode("variants")[0]

            Product(
                productCode,
                variantNode.getStringValue("price"),
                newPrice,
                variantNode.getStringValue("compare_at_price"),
                newQuantity,
                variantNode.getNumberValue("inventory_item_id"),
                variantNode.getNumberValue("id")
            )
        }.forEach { (productCode,
                        currentPrice, newPrice, compareAtPrice,
                        newQuantity,
                        inventoryItemId, variantId) ->

            setProductPriceFunction.apply(productCode, variantId, newPrice ?: currentPrice, compareAtPrice)

            newQuantity?.let { updateProductInventoryFunction.apply(inventoryItemId, it) }
        }
}

data class Product(
    val productCode: String,
    val currentPrice: String,
    val newPrice: String?,
    val compareAtPrice: String,
    val newQuantity: String?,
    val inventoryItemId: String,
    val variantId: String
)

data class ProductUpdate(
    val productCode: String,
    val newPrice: String?,
    val newQuantity: String?
)

fun allProductCodesToPrices(): Set<ProductUpdate> =
    readLines("all-products-with-prices.txt")
        .drop(1)
        .map {
            val array = it.split(",")

            ProductUpdate(
                array[0].toLowerCase(),
                array[1].trim().ifEmpty { null },
                array[3].trim().ifEmpty { null }
            )
        }.toSet()