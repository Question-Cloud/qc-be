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
    
    api("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    
    api(project(":qc-core:qc-user:qc-user-core"))
    implementation(project(":qc-common"))
    implementation(project(":qc-core:qc-user:qc-user-rdb"))
    implementation(project(":qc-core:qc-user:qc-user-social"))
    implementation(project(":qc-core:qc-user:qc-user-redis"))
    
    implementation(project(":qc-internal-api-interface:qc-point-internal-api-interface"))
    
    testImplementation(testFixtures(project(":qc-core:qc-user:qc-user-core")))
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