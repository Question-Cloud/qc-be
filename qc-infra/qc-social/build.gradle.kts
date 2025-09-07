import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation(project(":qc-infra:qc-http"))
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
