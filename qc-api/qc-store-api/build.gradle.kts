import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
}

group = "com.eager"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qc-common"))
    implementation(project(":qc-event"))

    implementation(project(":qc-domain:qc-question"))
    implementation(project(":qc-domain:qc-review"))
    implementation(project(":qc-domain:qc-cart"))
    implementation(project(":qc-domain:qc-user"))
    implementation(project(":qc-domain:qc-creator"))

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