package com.ingco.shopify.api.functions

import argo.jdom.JdomParser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.lang.RuntimeException

class GetProductInventoryFunction(private val storeAddress: String, val apiCredentials: String) {

    fun apply(inventoryItemId: String): Int {
        val (request, response, result) = "https://$storeAddress/admin/api/2020-04/inventory_levels.json"
            .httpGet(listOf("inventory_item_ids" to inventoryItemId))
            .header(
                "Content-Type" to "application/json",
                "Authorization" to "Basic $apiCredentials="
            )
            .responseString()

        when (result) {
            is Result.Failure -> {
                throw result.getException()
            }
            is Result.Success -> {
                return JdomParser().parse(result.get()).getArrayNode("inventory_levels").first()
                    .getNumberValue("available").toInt()
            }
            else -> throw RuntimeException("couldn't get product availability")
        }
    }
}