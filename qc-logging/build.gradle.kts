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
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.0")
    
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation(platform("io.micrometer:micrometer-tracing-bom:latest.release"))
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")
    
    implementation("ch.qos.logback:logback-classic")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    
    compileOnly("org.springframework:spring-web")
    compileOnly("jakarta.servlet:jakarta.servlet-api")
    
    testImplementation(kotlin("test"))
}