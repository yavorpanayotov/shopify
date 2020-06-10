package com.ingco.shopify.api

import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ProductStoreTest {

    private val config = loadConfig()
    private val store = ProductStore(config[shopify.fullStoreUrl], config[shopify.apiCredentials])

    @Test
    fun `gets products`() {
        store.init()

        assertThat(store.size()).isGreaterThan(300)
    }
}