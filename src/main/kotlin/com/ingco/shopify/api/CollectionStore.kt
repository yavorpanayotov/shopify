package com.ingco.shopify.api

import argo.jdom.JdomParser
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify

class CollectionStore(private val storeAddress: String, private val apiCredentials: String) {

    private lateinit var productIdsToCollectionIds: List<Pair<Long, Long>>
    private lateinit var collectionIdsToCollections: List<Pair<Long, String>>

    fun init() {
        val (_, response, result) = "https://$storeAddress/admin/api/2020-04/collects.json"
            .httpGet(listOf("limit" to "250"))
            .header(headers())
            .responseString()

        val first250Collects = JdomParser().parse(result.get()).getArrayNode("collects")

        val linkHeader = response.headers["Link"].first()
        val (_, _, result2) = "https://$storeAddress/admin/api/2020-04/collects.json"
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
        val theRest = JdomParser().parse(result2.get()).getArrayNode("collects")

        productIdsToCollectionIds = (first250Collects + theRest)
            .map {
                it.getNumberValue("product_id").toLong() to it.getNumberValue("collection_id").toLong()
            }

        initCollectionIdsToCollections()
    }

    fun collectionFor(productId: Long): String {
        val collectionId = productIdsToCollectionIds.first { it.first == productId }.second
        return collectionIdsToCollections.first { it.first == collectionId }.second
    }

    private fun initCollectionIdsToCollections() {
        val (_, _, result) = "https://ingco-bulgaria.myshopify.com/admin/api/2020-04/custom_collections.json"
            .httpGet()
            .header(headers())
            .responseString()

        collectionIdsToCollections = JdomParser().parse(result.get()).getArrayNode("custom_collections")
            .map { it.getNumberValue("id").toLong() to it.getStringValue("title") }
    }

    private fun headers(): Map<String, Any> =
        mapOf("Content-Type" to "application/json", "Authorization" to "Basic $apiCredentials=")

    companion object {
        fun init(): CollectionStore {
            val config = loadConfig()
            val collectionStore = CollectionStore(config[shopify.fullStoreUrl], config[shopify.apiCredentials])
            collectionStore.init()
            return collectionStore
        }
    }
}