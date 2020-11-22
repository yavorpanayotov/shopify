package com.ingco.shopify.api

import kotlin.streams.toList

fun main() {
    val productStore = ProductStore.init()

    val productsInfo: List<Item> = productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .map { (productCode, data) ->
            val variant = data.getArrayNode("variants").get(0)
            val images = data.getArrayNode("images")

            Item(
                productCode,
                data.getStringValue("title"),
                data.getStringValue("body_html").replace(Regex("<.*?>\n?"), ""),
                variant.getStringValue("price"),
                data.getNode("image").getStringValue("src"),
                images.stream().map { it.getStringValue("src") }.toList()
            )
        }

//    val xmlMapper = XmlMapper()
//    xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
//    println(xmlMapper.writeValueAsString(productsInfo))
}

data class Item(
    val productCode: String,
    val title: String,
    val description: String,
    val price: String,
    val main_image: String,
    val all_images: List<String>
)

