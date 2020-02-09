package com.ingco.shopify.config

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType

object shopify : PropertyGroup() {
    val store by stringType
    val email by stringType
    val password by stringType

    val fullStoreUrl by stringType
    val apiCredentials by stringType
}