package com.ingco.shopify.api

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class ProductStore(private val storeAddress: String, private val apiCredentials: String) {
    fun product(productCode: String) {
        val (request, response, result) = "https://$storeAddress/admin/api/2020-01/products.json"
            .httpGet()
            .header(
                "Content-Type" to "application/json",
                "Authorization" to "Basic $apiCredentials"
            )
            .responseString()

        when (result) {
            is Result.Failure -> {
                println(result.getException())
            }
            is Result.Success -> {
                println(result.get())
            }
        }
    }
}