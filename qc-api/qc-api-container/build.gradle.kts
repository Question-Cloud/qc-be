plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
    id("com.epages.restdocs-api-spec") version "0.19.2"
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
    implementation(project(":qc-event"))

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

tasks.register<Copy>("collectSnippets") {
    dependsOn(":qc-api:qc-user-api:test")
    dependsOn(":qc-api:qc-payment-api:test")
    dependsOn(":qc-api:qc-creator-api:test")
    dependsOn(":qc-api:qc-post-api:test")
    dependsOn(":qc-api:qc-store-api:test")
    dependsOn(":qc-api:qc-auth-api:test")
    dependsOn(":qc-api:qc-library-api:test")
    dependsOn(":qc-api:qc-subscribe-api:test")
    dependsOn(":qc-api:qc-workspace-api:test")

    from("../qc-user-api/build/generated-snippets")
    from("../qc-payment-api/build/generated-snippets")
    from("../qc-creator-api/build/generated-snippets")
    from("../qc-post-api/build/generated-snippets")
    from("../qc-store-api/build/generated-snippets")
    from("../qc-auth-api/build/generated-snippets")
    from("../qc-library-api/build/generated-snippets")
    from("../qc-subscribe-api/build/generated-snippets")
    from("../qc-workspace-api/build/generated-snippets")

    into("build/generated-snippets")
}

// OpenAPI 3 생성 설정
openapi3 {
    setServer("http://localhost:8080")
    title = "Question Cloud API"
    description = "Question Cloud Unified API Documentation"
    version = "1.1.1"
    format = "yaml"
}



tasks.test {
    useJUnitPlatform()
}