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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.0")

    api(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.3.0"))
    api("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    api("io.awspring.cloud:spring-cloud-aws-starter-sns")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("software.amazon.awssdk:netty-nio-client:2.31.32")

    implementation(project(":qc-common"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}