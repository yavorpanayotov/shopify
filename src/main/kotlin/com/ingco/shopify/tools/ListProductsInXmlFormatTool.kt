package com.ingco.shopify.tools

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.ingco.shopify.api.CollectionStore
import com.ingco.shopify.api.InventoryStore
import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.productCode
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import kotlin.streams.toList

fun main() {
    val productStore = ProductStore.init()
    val collectionStore = CollectionStore.init()
    val inventoryStore = InventoryStore.init(productStore.inventoryItemIds())

    var products: List<Product> = productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .map { (productCode, data) ->
            val variant = data.getArrayNode("variants")[0]
            val images = data.getArrayNode("images")
            val price = variant.getStringValue("price").toBigDecimal()
            val title = data.getStringValue("title")
            val inventoryItemId = variant.getNumberValue("inventory_item_id").toLong()
            val productId = variant.getNumberValue("product_id").toLong()
            val bodyHtml = data.getStringValue("body_html")

            val pictureUrls: List<String> =
                (listOf(data.getNode("image").getStringValue("src")) +
                        images.stream().map { it.getStringValue("src") }.toList())
                    .distinct()

            Product(
                title,
                "INGCO",
                productCode,
                productCode,
                price.multiply("0.8".toBigDecimal()).setScale(2, HALF_UP),
                "BGN",
                price,
                "BGN",
                inventoryStore.availableCount(inventoryItemId) > 0,
                collectionStore.collectionFor(productId).toString(),
                title,
                bodyHtml.replace(Regex("<.*?>\n?"), ""),
                variant.getStringValue("price"),
                Pictures(pictureUrls.withIndex().map { Picture(it.value, it.index + 1) }),
                Regex("""class="video-container".*?src="(.*?)\?rel=0"""").find(bodyHtml)?.groups?.get(1)?.value ?: ""
            )
        }

    val shoes = products.first { it.product_name == "ЗАЩИТНИ РАБОТНИ ОБУВКИ INGCO SSH12SB" }
    products = products - shoes + listOf("40", "41", "42", "43", "44", "45")
        .map { shoes.copy(size = it) }

    val jacket = products.first { it.product_name == "ВОДОУСТОЙЧИВО И ВЕТРОУСТОЙЧИВО ДИШАЩО ЯКЕ INGCO HJATL2281" }
    products = products - jacket + listOf("L", "XL")
        .map { jacket.copy(size = it) }

    val xmlMapper = XmlMapper()
    xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
    val output = xmlMapper.writeValueAsString(Products(products))

    println(output)
    File("products.xml").writeText("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n$output")
}

data class Product(
    val product_name: String,
    val product_brand: String,
    val product_id: String,
    val product_code: String,
    val dealer_price: BigDecimal,
    val dealer_currency: String,
    val sales_price: BigDecimal,
    val sale_currency: String,
    val available: Boolean,
    val category: String,
    val description: String,
    val full_description: String,
    val price: String,
    val pictures: Pictures,
    val video: String,
    val size: String? = null
)

@JacksonXmlRootElement(localName = "pictures")
data class Pictures(
    @JacksonXmlElementWrapper(useWrapping = false)
    val picture: List<Picture>
)

data class Picture(
    val picture_url: String,
    val picture_order: Int
)

@JacksonXmlRootElement(localName = "products")
data class Products(
    @JacksonXmlElementWrapper(useWrapping = false)
    val product: List<Product>
)

