import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    id("java-test-fixtures")
    kotlin("plugin.spring") version "2.1.10"
    kotlin("plugin.jpa") version "2.1.10"
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation("org.springframework.security:spring-security-crypto")
    implementation(project(":qc-common"))
    implementation(project(":qc-internal-api-interface:qc-user-internal-api-interface"))
    
    testImplementation(kotlin("test"))
    testFixturesImplementation(project(":qc-test-utils"))
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