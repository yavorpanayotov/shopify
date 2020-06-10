package com.ingco.shopify.api

import argo.jdom.JdomParser
import argo.jdom.JsonNode
import com.github.kittinunf.fuel.httpGet
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify

class ProductStore(private val storeAddress: String, private val apiCredentials: String) {

    private lateinit var products: List<JsonNode>

    fun products(): List<JsonNode> = products

    fun size(): Int = products.size

    fun init() {
        val (request, response, result) = "https://$storeAddress/admin/api/2020-04/products.json"
            .httpGet(listOf("limit" to "250"))
            .header(headers())
            .responseString()

        val first250Products = JdomParser().parse(result.get()).getArrayNode("products")

        val linkHeader = response.headers["Link"].first()
        val (request2, response2, result2) = "https://$storeAddress/admin/api/2020-04/products.json"
            .httpGet(
                listOf(
                    "limit" to "250",
                    "page_info" to linkHeader.substring(
                        linkHeader.indexOf("page_info") + "page_info=".length,
                        linkHeader.indexOf(">;")
                    )
                )
            )
            .header(headers())
            .responseString()
        val theRest = JdomParser().parse(result2.get()).getArrayNode("products")

        products = first250Products + theRest
    }

    private fun headers(): Map<String, Any> =
        mapOf("Content-Type" to "application/json", "Authorization" to "Basic $apiCredentials=")

    companion object {
        fun init() : ProductStore {
            val config = loadConfig()
            val productStore = ProductStore(config[shopify.fullStoreUrl], config[shopify.apiCredentials])
            productStore.init()
            return productStore
        }
    }
}