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
    implementation("org.springframework:spring-context")
    implementation("com.squareup.okhttp3:okhttp")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}