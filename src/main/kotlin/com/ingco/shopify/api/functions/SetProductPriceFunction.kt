package com.ingco.shopify.api.functions

import argo.format.PrettyJsonFormatter
import argo.jdom.JsonNodeFactories.*
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result

class SetProductPriceFunction(val storeAddress: String, val apiCredentials: String) {

    fun apply(productCode: String, id: String, price: String, compareAtPrice: String) {
        val body = PrettyJsonFormatter().format(
            `object`(
                field(
                    "variant", `object`(

                        field("id", number(id)),
                        field("price", string(price)),
                        field("compare_at_price", string(compareAtPrice))
                    )
                )
            )
        )
        val url = "https://$storeAddress/admin/api/2020-04/variants/$id.json"
        val (request, response, result) = url
            .httpPut()
            .body(
                body
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
                println("$productCode price updated $price")
            }
        }
    }
}