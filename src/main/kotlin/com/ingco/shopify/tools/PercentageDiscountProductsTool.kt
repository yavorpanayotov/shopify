package com.ingco.shopify.tools

import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.functions.SetProductPriceFunction
import com.ingco.shopify.api.productCode
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP

fun main() {
    throw IllegalStateException("you sure?")

    val config = loadConfig()
    val setProductPriceFunction = SetProductPriceFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])
    val discountFactor = BigDecimal("0.8")
    val productStore = ProductStore.init()

    productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .forEach { (productCode, data) ->
            val variantNode = data.getArrayNode("variants")[0]

            val currentPrice = variantNode.getStringValue("price")
            val newPrice = discount(currentPrice, discountFactor)
            println("disc. $productCode from $currentPrice to $newPrice")

            setProductPriceFunction.apply(
                productCode,
                variantNode.getNumberValue("id"),
                newPrice,
                currentPrice
            )
        }
}

private fun discount(value: String, discountFactor: BigDecimal): String {
    return BigDecimal(value).multiply(discountFactor).setScale(2, HALF_UP).toPlainString()
}