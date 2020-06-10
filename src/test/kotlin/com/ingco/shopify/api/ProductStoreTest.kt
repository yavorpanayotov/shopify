package com.ingco.shopify.api

import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify
import org.junit.jupiter.api.Test

internal class ProductStoreTest {

    val config = loadConfig()
    val store = ProductStore(config[shopify.fullStoreUrl], config[shopify.apiCredentials])

    @Test
    fun `gets products`() {
        store.product("some product id")
    }
}