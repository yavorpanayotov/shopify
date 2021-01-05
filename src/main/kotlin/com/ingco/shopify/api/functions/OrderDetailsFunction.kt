package com.ingco.shopify.api.functions

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class OrderDetailsFunction(val storeAddress: String, val apiCredentials: String) {

    fun apply(orderId: String) {
        val (request, response, result) = "https://$storeAddress/admin/api/2020-04/orders/$orderId.json"
            .httpGet()
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
                println(String(response.body().toByteArray()))
            }
        }
    }
}