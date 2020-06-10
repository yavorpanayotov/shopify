package com.ingco.shopify.api

import argo.format.PrettyJsonFormatter
import argo.jdom.JsonNode
import argo.jdom.JsonNodeFactories
import argo.jdom.JsonNodeFactories.`object`
import argo.jdom.JsonNodeFactories.field
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result

class AddTagsProductFunction(val storeAddress: String, val apiCredentials: String) {

    fun apply(tag: String, it: JsonNode) {
        val (request, response, result) = "https://$storeAddress/admin/api/2020-04/products/${it.getNumberValue("id")}.json"
            .httpPut()
            .body(
                PrettyJsonFormatter().format(
                    `object`(
                        field(
                            "product", `object`(
                                field("id", JsonNodeFactories.number(it.getNumberValue("id"))),
                                field(
                                    "tags",
                                    JsonNodeFactories.string(it.getStringValue("tags") + ", $tag")
                                )
                            )
                        )
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
                println("${it.getStringValue("handle")}: ${it.getStringValue("id")} updated")
            }
        }
    }
}