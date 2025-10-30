import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context")
    compileOnly("org.springframework:spring-tx")
    
    implementation(project(":qc-core:qc-question:qc-question-core"))
    implementation(project(":qc-internal-api-interface:qc-question-internal-api-interface"))
    implementation(project(":qc-common"))
    
    testImplementation(kotlin("test"))
    testImplementation(project(":qc-core:qc-question:qc-question-fixture"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}