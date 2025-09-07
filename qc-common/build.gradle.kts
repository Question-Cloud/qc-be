import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
    kotlin("plugin.jpa") version "2.1.10"
}

group = "com.eager"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(project(":qc-infra::qc-rdb"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}