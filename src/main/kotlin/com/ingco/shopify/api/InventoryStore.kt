package com.ingco.shopify.api

import argo.jdom.JdomParser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify
import java.lang.RuntimeException

class InventoryStore(private val storeAddress: String, private val apiCredentials: String) {

    private lateinit var inventoryItems: List<Pair<Long, Int>>

    fun init(inventoryItemIds: List<String>) {
        inventoryItems = inventoryItemIds
            .chunked(50)
            .flatMap { toInventoryItems(it) }
    }

    fun availableCount(inventoryItemId: Long) = inventoryItems.first { it.first == inventoryItemId }.second

    private fun toInventoryItems(inventoryItemIds: List<String>): List<Pair<Long, Int>> {
        val (_, _, result) = "https://$storeAddress/admin/api/2020-04/inventory_levels.json"
            .httpGet(listOf("inventory_item_ids" to inventoryItemIds.joinToString(",")))
            .header(headers())
            .responseString()

        when (result) {
            is Result.Failure -> {
                throw result.getException()
            }
            is Result.Success -> {
                return JdomParser().parse(result.get())
                    .getArrayNode("inventory_levels")
                    .map { it.getNumberValue("inventory_item_id").toLong() to it.getNumberValue("available").toInt() }
            }
            else -> throw RuntimeException("couldn't get product availability")
        }
    }

    private fun headers(): Map<String, Any> =
        mapOf("Content-Type" to "application/json", "Authorization" to "Basic $apiCredentials=")

    companion object {
        fun init(inventoryItemIds: List<String>): InventoryStore {
            val config = loadConfig()
            val inventoryStore = InventoryStore(config[shopify.fullStoreUrl], config[shopify.apiCredentials])
            inventoryStore.init(inventoryItemIds)
            return inventoryStore
        }
    }
}