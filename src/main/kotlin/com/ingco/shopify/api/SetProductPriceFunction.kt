package com.ingco.shopify.api

import argo.format.PrettyJsonFormatter
import argo.jdom.JsonNodeFactories
import argo.jdom.JsonNodeFactories.`object`
import argo.jdom.JsonNodeFactories.field
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import java.math.BigDecimal
import java.math.RoundingMode

class SetProductPriceFunction(val storeAddress: String, val apiCredentials: String) {

    fun apply(productCode: String, id: String, price: String) {
        val body = PrettyJsonFormatter().format(
            `object`(
                field(
                    "variant", `object`(


                        field("id", JsonNodeFactories.number(id)),
                        field("price", JsonNodeFactories.string(BigDecimal(price).multiply(BigDecimal("0.8")).setScale(2, RoundingMode.HALF_UP).toPlainString())),
                        field("compare_at_price", JsonNodeFactories.string(price))
                        //field("compare_at_price", JsonNodeFactories.nullNode())
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