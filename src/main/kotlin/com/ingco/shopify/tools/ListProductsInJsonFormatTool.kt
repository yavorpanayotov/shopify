package com.ingco.shopify.tools

import argo.format.PrettyJsonFormatter
import argo.jdom.JsonNodeFactories.*
import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.productCode
import kotlin.streams.toList

fun main() {
    val productStore = ProductStore.init()

    val productsInfo = productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .map { (productCode, data) ->
            val variant = data.getArrayNode("variants").get(0)
            val images = data.getArrayNode("images")

            `object`(
                field("sku", string(productCode)),
                field("name", data.getNode("title")),
                field("price", variant.getNode("price")),
                field("main_image", data.getNode("image").getNode("src")),
                field("all_images", array(images.stream().map { it.getNode("src") }.toList())),
                field("body_html", string(data.getStringValue("body_html").replace(Regex("<.*?>\n?"), ""))),
                field("tags", data.getNode("tags"))
            )
        }

    println(PrettyJsonFormatter().format(`object`(field("all_products", array(productsInfo)))))
}