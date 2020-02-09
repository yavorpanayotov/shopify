package com.ingco.shopify.api

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result;
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify

fun main() {
    val config = loadConfig()
    val (request, response, result) = "https://${config[shopify.fullStoreUrl]}/admin/api/2020-01/shop.json"
        .httpGet()
        .header(
            "Content-Type" to "application/json",
            "Authorization" to "Basic ${config[shopify.apiCredentials]}="
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