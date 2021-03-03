package com.ingco.shopify.api.functions

import argo.format.PrettyJsonFormatter
import argo.jdom.JdomParser
import argo.jdom.JsonNodeFactories
import argo.jdom.JsonNodeFactories.`object`
import argo.jdom.JsonNodeFactories.field
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

class UpdateProductInventoryFunction(private val storeAddress: String, val apiCredentials: String) {

    fun apply(productCode: String, inventoryItemId: String, quantity: String) {
        val (request, response, result) = "https://$storeAddress/admin/api/2020-04/inventory_levels.json"
            .httpGet(listOf("inventory_item_ids" to inventoryItemId))
            .header(
                "Content-Type" to "application/json",
                "Authorization" to "Basic $apiCredentials="
            )
            .responseString()

        when (result) {
            is Result.Failure -> {
                println(result.getException())
            }
            is Result.Success -> {
                val locationId = JdomParser().parse(result.get()).getArrayNode("inventory_levels").first().getNumberValue("location_id")

                val (request, response, result) = "https://$storeAddress/admin/api/2020-04/inventory_levels/set.json"
                    .httpPost()
                    .body(
                        PrettyJsonFormatter().format(
                            `object`(
                                field("location_id", JsonNodeFactories.number(locationId)),
                                field("inventory_item_id", JsonNodeFactories.number(inventoryItemId)),
                                field("available", JsonNodeFactories.number(quantity))
                            )
                        )
                    )
                    .header(
                        "Content-Type" to "application/json",
                        "Authorization" to "Basic $apiCredentials="
                    )
                    .responseString()

                when (result) {
                    is Result.Failure -> {
                        println(result.getException())
                    }
                    is Result.Success -> {
                        println("$productCode new quantity $quantity")
                    }
                }
            }
        }
    }
}