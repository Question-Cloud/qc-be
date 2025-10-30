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
    implementation("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.springframework:spring-tx")
    
    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.3.0"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sns")
    
    api("software.amazon.awssdk:netty-nio-client:2.31.32")
    
    implementation(project(":qc-core:qc-question:qc-question-api"))
    implementation(project(":qc-core:qc-cart:qc-cart-api"))
    implementation(project(":qc-core:qc-point:qc-point-api"))
    implementation(project(":qc-core:qc-pay:qc-pay-api"))
    implementation(project(":qc-core:qc-creator:qc-creator-api"))
    
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