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
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    implementation(project(":qc-common"))
    implementation(project(":qc-domain:qc-user"))
    implementation(project(":qc-domain:qc-question"))

    implementation(project(":qc-api:qc-auth-api"))
    implementation(project(":qc-api:qc-store-api"))

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