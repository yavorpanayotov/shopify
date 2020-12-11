package com.ingco.shopify.tools

import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.functions.SetProductPriceFunction
import com.ingco.shopify.api.productCode
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify

fun main() {

    val config = loadConfig()
    val setProductPriceFunction = SetProductPriceFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])
    val productStore = ProductStore.init()

    productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .forEach { (productCode, data) ->
            val variantNode = data.getArrayNode("variants")[0]

            val compareAtPrice = variantNode.getNullableStringValue("compare_at_price")

            if (compareAtPrice != null) {
                println("rev. $productCode from ${variantNode.getStringValue("price")} to $compareAtPrice")

                setProductPriceFunction.apply(
                    productCode,
                    variantNode.getNumberValue("id"),
                    compareAtPrice,
                    null
                )
            } else {
                println("skipping $productCode")
            }
        }
}