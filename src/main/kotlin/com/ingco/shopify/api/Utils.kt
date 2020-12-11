package com.ingco.shopify.api

import java.io.File

fun readLines(file: String) = File(object {}.javaClass.getResource("/$file").file).readLines()

fun productCode(handle: String): String {
    val parts = handle.split("-")
    val handleStart = parts.indexOf("ingco") + 1

    if (parts.contains("rs4501")) return "rs4501.2"
    if (parts[handleStart] == "ing") return "${parts[handleStart]}-${parts[handleStart + 1]}"

    if (hasMoreThanTwoParts(handleStart, parts)
        && (
                nextTwoPartsAreSingleChars(parts, handleStart)
                        || secondPartShouldBeExcluded(parts, handleStart))
    ) {
        return parts.subList(handleStart, handleStart + 1).joinToString("-")
    }

    if (hasMoreThanOnePart(handleStart, parts)
        && (
                nextPartIsSingleChar(parts, handleStart)
                        || nextPartIsXl(parts, handleStart))
    ) {
        return parts.subList(handleStart, handleStart + 2).joinToString("-")
    }

    return parts.subList(handleStart, parts.size).first()
}

private fun hasMoreThanOnePart(handleStart: Int, parts: List<String>) = handleStart < parts.size - 1

private fun hasMoreThanTwoParts(handleStart: Int, parts: List<String>) = handleStart < parts.size - 2

private fun secondPartShouldBeExcluded(parts: List<String>, handleStart: Int) = parts[handleStart + 2].endsWith("ah")

private fun nextPartIsSingleChar(parts: List<String>, handleStart: Int) = parts[handleStart + 1].length == 1

private fun nextTwoPartsAreSingleChars(parts: List<String>, handleStart: Int) = parts[handleStart + 1].length == 1 && parts[handleStart + 2].length == 1

private fun nextPartIsXl(parts: List<String>, handleStart: Int) = parts[handleStart + 1] == "xl"

