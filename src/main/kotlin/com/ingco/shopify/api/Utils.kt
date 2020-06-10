package com.ingco.shopify.api

import java.io.File

fun readLines(file: String) = File(object{}.javaClass.getResource("/$file").file).readLines()