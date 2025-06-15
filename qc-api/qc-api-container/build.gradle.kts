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
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation(platform("io.micrometer:micrometer-tracing-bom:latest.release"))
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    implementation(project(":qc-common"))
    implementation(project(":qc-logging"))


    implementation(project(":qc-domain:qc-user"))
    implementation(project(":qc-domain:qc-question"))

    implementation(project(":qc-api:qc-auth-api"))
    implementation(project(":qc-api:qc-store-api"))
    implementation(project(":qc-api:qc-payment-api"))
    implementation(project(":qc-api:qc-creator-api"))
    implementation(project(":qc-api:qc-post-api"))
    implementation(project(":qc-api:qc-subscribe-api"))
    implementation(project(":qc-api:qc-library-api"))
    implementation(project(":qc-api:qc-user-api"))
    implementation(project(":qc-api:qc-workspace-api"))

    testImplementation(kotlin("test"))
}

sourceSets {
    main {
        resources {
            srcDir("../../qc-config")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}