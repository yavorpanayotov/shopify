package com.ingco.shopify.tools

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.ingco.shopify.api.ProductStore
import com.ingco.shopify.api.functions.GetProductCategoryFunction
import com.ingco.shopify.api.functions.GetProductInventoryFunction
import com.ingco.shopify.api.productCode
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify
import java.math.BigDecimal
import java.math.RoundingMode.HALF_UP
import kotlin.streams.toList

fun main() {
    val productStore = ProductStore.init()

    val config = loadConfig()
    val getProductInventoryFunction = GetProductInventoryFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])
    val getProductCategoryFunction = GetProductCategoryFunction(config[shopify.fullStoreUrl], config[shopify.apiCredentials])

    val products: List<Product> = productStore.products()
        .map { it.getStringValue("handle") to it }
        .map { productCode(it.first) to it.second }
        .map { (productCode, data) ->
            val variant = data.getArrayNode("variants")[0]
            val images = data.getArrayNode("images")
            val price = variant.getStringValue("price").toBigDecimal()
            val title = data.getStringValue("title")

            val pictureUrls: List<String> =
                (listOf(data.getNode("image").getStringValue("src")) +
                        images.stream().map { it.getStringValue("src") }.toList())
                    .distinct()

            Thread.sleep(1000)

            Product(
                title,
                "INGCO",
                productCode,
                productCode,
                price.multiply("0.8".toBigDecimal()).setScale(2, HALF_UP),
                "BGN",
                price,
                "BGN",
                getProductInventoryFunction.apply(variant.getNumberValue("inventory_item_id")) > 0,
                getProductCategoryFunction.apply(variant.getNumberValue("product_id").toLong()),
                title,
                data.getStringValue("body_html").replace(Regex("<.*?>\n?"), ""),
                variant.getStringValue("price"),
                Pictures(pictureUrls.withIndex().map { Picture(it.value, it.index + 1) })
            )
        }

    val xmlMapper = XmlMapper()
    xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
    println(xmlMapper.writeValueAsString(Products(products)))
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
    val pictures: Pictures
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

