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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.springframework:spring-tx")
    
    implementation(project(":qc-internal-api-interface:qc-user-internal-api-interface"))
    implementation(project(":qc-internal-api-interface:qc-creator-internal-api-interface"))
    
    implementation(project(":qc-core:qc-question:qc-question-core"))
    implementation(project(":qc-common"))
    implementation(project(":qc-event"))
    
    testImplementation(project(":qc-core:qc-question:qc-question-fixture"))
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