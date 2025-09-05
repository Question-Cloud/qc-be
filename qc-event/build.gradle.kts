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
    
    api(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.3.0"))
    api("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    api("io.awspring.cloud:spring-cloud-aws-starter-sns")
    
    compileOnly("org.springframework:spring-tx")
    
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    
    api("software.amazon.awssdk:netty-nio-client:2.31.32")
    
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.0")
    
    implementation(project(":qc-common"))
    implementation(project(":qc-rdb"))
    implementation(project(":qc-external-pg-api"))
    
    testImplementation(kotlin("test"))
    testImplementation(project(":qc-api-container"))
}

tasks.test {
    useJUnitPlatform()
}
