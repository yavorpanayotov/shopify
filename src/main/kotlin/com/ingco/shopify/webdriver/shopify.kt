package com.ingco.shopify.webdriver

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType

object shopify : PropertyGroup() {
    val store by stringType
    val email by stringType
    val password by stringType
}