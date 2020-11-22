package com.ingco.shopify.api

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlin.streams.toList

fun main() {
    val productStore = ProductStore.init()

    val productsInfo = productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .map { (productCode, data) ->
            val variant = data.getArrayNode("variants").get(0)
            val images = data.getArrayNode("images")

            listOf(
                productCode,
                data.getStringValue("title"),
                data.getStringValue("body_html").replace(Regex("<.*?>\n?"), ""),
                variant.getStringValue("price"),
                data.getNode("image").getStringValue("src"),
                images.stream().map { it.getStringValue("src") }.toList()
            )
        }

    val data = listOf(listOf("sku", "name", "description", "price", "main_image", "additional_images")) + productsInfo
    csvWriter().open("all_products.csv") { writeAll(data) }
}