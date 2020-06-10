package com.ingco.shopify.api

import java.io.File

fun readLines(file: String) = File(ProductStore.javaClass::class.java.getResource("/$file").file).readLines()