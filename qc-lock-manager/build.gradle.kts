plugins {
    id("org.jetbrains.kotlin.jvm")
    kotlin("plugin.spring") version "2.1.10"
}

dependencies {
    implementation("org.redisson:redisson:3.36.0")
    implementation("org.springframework:spring-context")
}

tasks.getByName<Jar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

kotlin {
    jvmToolchain(17)
}