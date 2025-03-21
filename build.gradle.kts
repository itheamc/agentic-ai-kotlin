plugins {
    kotlin("jvm") version "2.1.10"
}

group = "com.itheamc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.langchain4j:langchain4j:1.0.0-beta2")
    // For OpenAi Model
    implementation("dev.langchain4j:langchain4j-open-ai:1.0.0-beta2")

    // OkHttp for HTTP Client
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}