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
    implementation(project(":qc-core:qc-cart:qc-cart-core"))
    implementation(project(":qc-core:qc-creator:qc-creator-core"))
    implementation(project(":qc-core:qc-pay:qc-pay-core"))
    implementation(project(":qc-core:qc-point:qc-point-core"))
    implementation(project(":qc-core:qc-post:qc-post-core"))
    implementation(project(":qc-core:qc-question:qc-question-core"))
    implementation(project(":qc-core:qc-review:qc-review-core"))
    implementation(project(":qc-core:qc-subscribe:qc-subscribe-core"))
    implementation(project(":qc-core:qc-user:qc-user-core"))
    
    implementation(project(":qc-common"))
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    
    api("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")
    
    api("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")
    
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