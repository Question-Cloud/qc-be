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
    implementation(project(":qc-domain:qc-question"))
    implementation(project(":qc-domain:qc-coupon"))
    implementation(project(":qc-domain:qc-pay"))
    implementation(project(":qc-domain:qc-point"))

    implementation(project(":qc-common"))
    implementation(project(":qc-event"))
    implementation(project(":qc-external-pg-api"))

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