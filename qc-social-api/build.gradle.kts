import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.spring") version "2.1.10"
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

kotlin {
    jvmToolchain(17)
}
