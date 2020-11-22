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
    implementation("net.sourceforge.argo:argo:5.13")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.7.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.9.4")

    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.4.2")
    testImplementation("org.assertj:assertj-core:3.12.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.2")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}