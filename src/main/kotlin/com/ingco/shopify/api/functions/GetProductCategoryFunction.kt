package com.ingco.shopify.api.functions

import argo.jdom.JdomParser
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.lang.RuntimeException

class GetProductCategoryFunction(private val storeAddress: String, val apiCredentials: String) {

    fun apply(productId: Long): String {
        val collectionId = collectionId(productId)

        val (request, response, result) = "https://$storeAddress/admin/api/2020-04/collections/$collectionId.json"
            .httpGet()
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
                return JdomParser().parse(result.get()).getNode("collection")
                    .getStringValue("title")
            }
            else -> throw RuntimeException("couldn't get collection id for product")
        }
    }

    private fun collectionId(productId: Long): Long {
        val (request, response, result) = "https://$storeAddress/admin/api/2020-04/collects.json"
            .httpGet(listOf("product_id" to productId))
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
                return JdomParser().parse(result.get()).getArrayNode("collects").first()
                    .getNumberValue("collection_id").toLong()
            }
            else -> throw RuntimeException("couldn't get collection id for product")
        }
    }
}