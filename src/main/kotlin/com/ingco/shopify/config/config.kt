package com.ingco.shopify.config

import com.natpryce.konfig.ConfigurationProperties
import java.io.File

fun loadConfig(): ConfigurationProperties {
    return ConfigurationProperties.fromFile(
        File("src/main/resources/shopify.properties")
    )
}