import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
    kotlin("plugin.jpa") version "2.1.10"
    id("java-test-fixtures")
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qc-common"))
    
    testImplementation(kotlin("test"))
    testFixturesImplementation(project(":qc-test-utils"))
    testFixturesImplementation(project(":qc-internal-api-interface:qc-user-internal-api-interface"))
    testFixturesImplementation(project(":qc-internal-api-interface:qc-creator-internal-api-interface"))
    testFixturesImplementation(project(":qc-internal-api-interface:qc-question-internal-api-interface"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}