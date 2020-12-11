package com.ingco.shopify.tools

import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.functions.SetProductPriceFunction
import com.ingco.shopify.api.productCode
import com.ingco.shopify.api.readLines
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP

fun main() {
    throw IllegalStateException("you sure?")

    val config = loadConfig()
    val setProductPriceFunction = SetProductPriceFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])
    val productStore = ProductStore.init()

    val promotionalProducts = promotionalProducts()

    println(productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .filter { (productCode, data) -> promotionalProducts.map { it.first }.contains(productCode) }
        .forEach { (productCode, data) ->
            val (promoProductCode, originalPrice, discountPercentage) = promotionalProducts.first { it.first == productCode }

            val variantNode = data.getArrayNode("variants")[0]

            val newPrice = discount(originalPrice, discountPercentage)
            println("disc. $productCode from $originalPrice to $newPrice")

            setProductPriceFunction.apply(
                productCode,
                variantNode.getNumberValue("id"),
                newPrice,
                originalPrice
            )
        })
}

private fun discount(value: String, percentage: String): String {
    val discount = value.toBigDecimal()
        .multiply(percentage.toBigDecimal().multiply(BigDecimal("0.01")).setScale(2, HALF_UP))
        .setScale(2, HALF_UP)

    return BigDecimal(value).minus(discount).toPlainString()
}

private fun promotionalProducts(): Set<Triple<String, String, String>> =
    readLines("promotional-prices-dec-2020.txt")
        .drop(1)
        .map {
            val array = it.split(",")

            Triple(
                array[0].trim().toLowerCase(),
                array[1].trim(),
                array[2].trim()
            )
        }.toSet()