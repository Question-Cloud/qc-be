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

    implementation(project(":qc-domain:qc-creator:qc-creator-core"))
    implementation(project(":qc-common"))
    implementation(project(":qc-event"))

    implementation(project(":qc-internal-api-interface:qc-user-internal-api-interface"))
    implementation(project(":qc-internal-api-interface:qc-question-internal-api-interface"))
    implementation(project(":qc-internal-api-interface:qc-post-internal-api-interface"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testImplementation("org.springframework:spring-tx")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.2")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
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