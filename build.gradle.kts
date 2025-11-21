plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("kapt") version "2.2.21"
    id("org.springframework.boot") version "3.5.8"
    id("io.spring.dependency-management") version "1.1.6"
    id("io.kotest") version "6.0.3"
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

subprojects {
    group = "com.eager.questioncloud"
    version = "0.0.1-SNAPSHOT"
    
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.kapt")
    apply(plugin = "io.kotest")
    
    repositories {
        mavenCentral()
    }
    
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
    
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("io.hypersistence:hypersistence-tsid:2.1.4")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.0")
        
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
        
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.kotest:kotest-runner-junit5:6.0.3")
        testImplementation("io.kotest:kotest-assertions-core:6.0.3")
        testImplementation("io.kotest:kotest-property:6.0.3")
        testImplementation("io.kotest:kotest-extensions-spring:6.0.3")
        testImplementation("com.ninja-squad:springmockk:4.0.2")
    }
    
    tasks.test {
        useJUnitPlatform()
    }
}

tasks.test {
    minHeapSize = "2g"
    maxHeapSize = "2g"
}