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
    
    implementation(project(":qc-core:qc-point:qc-point-core"))
    implementation(project(":qc-internal-api-interface:qc-point-internal-api-interface"))
    
    testImplementation(kotlin("test"))
}