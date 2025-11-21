import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
    kotlin("plugin.jpa") version "2.1.10"
    id("io.sentry.jvm.gradle") version "5.12.2"
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.springframework:spring-tx")
    
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.3.0"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sns")
    
    implementation(platform("software.amazon.awssdk:bom:2.39.0"))
    implementation("software.amazon.awssdk:sqs")
    implementation("software.amazon.awssdk:netty-nio-client")
    
    implementation(project(":qc-common"))
    
    testImplementation(kotlin("test"))
    testImplementation(project(":qc-api-container"))
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}