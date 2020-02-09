plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
    implementation("com.github.kittinunf.fuel:fuel:2.2.1")
    implementation("com.natpryce:konfig:1.6.10.0")
}