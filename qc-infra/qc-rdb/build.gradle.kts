import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
    kotlin("plugin.jpa") version "2.1.10"
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qc-common"))
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")
    compileOnly("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    compileOnly("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation(kotlin("test"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}