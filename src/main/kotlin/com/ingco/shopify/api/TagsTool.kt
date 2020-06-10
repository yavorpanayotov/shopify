package com.ingco.shopify.api

import argo.format.PrettyJsonFormatter
import argo.jdom.JdomParser
import argo.jdom.JsonNodeFactories
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import com.ingco.shopify.config.loadConfig
import com.ingco.shopify.config.shopify
import com.natpryce.konfig.ConfigurationProperties

fun main() {
    val config = loadConfig()
    val (request1, response1, result1) = "https://${config[shopify.fullStoreUrl]}/admin/api/2020-04/products.json"
        .httpGet(listOf("limit" to "250"))
        .header(
            "Content-Type" to "application/json",
            "Authorization" to "Basic ${config[shopify.apiCredentials]}="
        )
        .responseString()

    processResponse(result1, config)

    val linkHeader = response1.headers["Link"].first()

    val (request2, response2, result2) = "https://${config[shopify.fullStoreUrl]}/admin/api/2020-04/products.json"
        .httpGet(listOf("limit" to "250", "page_info" to linkHeader.substring(linkHeader.indexOf("page_info") + "page_info=".length, linkHeader.indexOf(">;"))))
        .header(
            "Content-Type" to "application/json",
            "Authorization" to "Basic ${config[shopify.apiCredentials]}="
        )
        .responseString()

    processResponse(result2, config)
}

private fun processResponse(
    result: Result<String, FuelError>,
    config: ConfigurationProperties
) {
    when (result) {
        is Result.Failure -> {
            println(result.getException())
        }
        is Result.Success -> {
            val jsonNode = JdomParser().parse(result.get()).getArrayNode("products")
            println(jsonNode.size)

            jsonNode
                .filter { it.isStringValue("handle") }
                .filter { industrialProductCodes().any { code -> it.getStringValue("handle").contains("-$code") } }
                .filter { !it.getStringValue("tags").contains("индустриален") }
                .forEach {
                    "https://${config[shopify.fullStoreUrl]}/admin/api/2020-04/products/${it.getNumberValue("id")}.json"
                        .httpPut()
                        .body(
                            PrettyJsonFormatter().format(
                            JsonNodeFactories.`object`(
                                JsonNodeFactories.field("product", JsonNodeFactories.`object`(
                                    JsonNodeFactories.field("id", JsonNodeFactories.number(it.getNumberValue("id"))),
                                    JsonNodeFactories.field("tags", JsonNodeFactories.string(it.getStringValue("tags") + ", индустриален"))
                                ))
                            )))
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
                            println("${it.getNumberValue("id")} updated")
                        }
                    }
                }
        }
    }
}

fun industrialProductCodes() : List<String> {
    return listOf("cdli2002e",
    "cidli2002e",
    "cidli2003",
    "ciwli2001",
    "cirli2002",
    "fbli2001e",
    "ckli2007",
    "fbli2002",
    "fcli2001e",
    "cagli1151",
    "apli2001",
    "mxli2001",
    "chtli2001",
    "cgsli2001",
    "cwli2001",
    "csdli0801",
    "id11008",
    "mx214008",
    "ag8006-2",
    "ag8508e",
    "ag110018",
    "ag24008e",
    "ap14008",
    "rh10508",
    "rh150068",
    "rh18008",
    "pdb13008",
    "pdb15006",
    "pdb17008",
    "cs18568",
    "js80028",
    "fs3208",
    "rt160028",
    "pl10508",
    "wlc15008",
    "hg200028",
    "spg5008",
    "ptwt215002",
    "ab8008",
    "gwp102",
    "gwp302",
    "ing-mma2006",
    "ing-cd2201",
    "aiw12562",
    "hkissd12101",
    "lm383",
    "hpwr18008",
    "dbh1210601",
    "dbh1210801",
    "dbh1210602",
    "dbh1210802",
    "dbh1211201",
    "dbh1211601",
    "dbh1210603",
    "dbh1210803",
    "dbh1210604",
    "dbh1210804",
    "akd1251",
    "akd3051",
    "akd3081",
    "akd2048",
    "akd2052",
    "sdb11ph213",
    "sdb11ph223",
    "sdb11sl413",
    "sdb11sl423",
    "sdb11pz213",
    "sdb21hl133",
    "sdb21ph223",
    "sdb21ph233",
    "amn65061",
    "epb820121",
    "epb820301",
    "hch8816",
    "hruh8308",
    "hruh8316",
    "hcp28188",
    "hcp28208",
    "hdcp28168",
    "hpp28258",
    "hhcp28180",
    "hhcp28200",
    "hhldcp28160",
    "hhlnp28200",
    "hkps28318",
    "hkhlps2831",
    "hkhlps2832",
    "hecp02180",
    "hwsp15608",
    "hwsp101",
    "hpw0810",
    "hpw0812",
    "hpw0818",
    "hmbc0808",
    "hbc0814",
    "hbc0824",
    "hkisd0608",
    "hkth10258",
    "hkts12122",
    "hkts12251",
    "hkthp21421",
    "htcs220971",
    "hgc0106",
    "hfc020501",
    "hfc020503",
    "hgp08023",
    "hgp08024",
    "hgp08033",
    "hgp08034",
    "hwba01482",
    "hr105",
    "hhf3088",
    "hsbb12246",
    "hcs3008",
    "huk6118",
    "hktfs0508",
    "hcg1709",
    "hcg0112",
    "hput08060",
    "hput08080",
    "hsfr24008",
    "chptb8715",
    "chptb8702",
    "hrht061001d",
    "hkspa1088",
    "hkspa1088-i",
    "hkspa2088",
    "hkspa3088",
    "hkspar1082",
    "hkspa1143",
    "hkspa2142",
    "hkspa3142",
    "hadw131088",
    "hadw131108",
    "hhk11091",
    "hhk13091",
    "hhkt8081",
    "htc04800ag",
    "hbj402",
    "hbj1202",
    "hfj302",
    "hsl08060",
    "hsl08120",
    "hbsl08060",
    "hhl013aaa2",
    "hgcg01-xl",
    "hgcg08-xl",
    "ssh12sb.40",
    "ssh12sb.41",
    "ssh12sb.42",
    "ssh12sb.43",
    "ssh12sb.44",
    "ssh12sb.45",
    "hrctl031.xl",
    "hps0308",
    "hhs6301",
    "hlt7401",
    "hfsw1808",
    "dws6003")
}