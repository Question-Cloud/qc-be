plugins {
    kotlin("plugin.spring") version "2.1.10"
    id("com.epages.restdocs-api-spec") version "0.19.4"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    compileOnly("org.springframework:spring-tx")

    implementation(platform("io.awspring.cloud:spring-cloud-aws-dependencies:3.3.0"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sns")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation(platform("io.micrometer:micrometer-tracing-bom:latest.release"))
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    implementation(project(":qc-core"))
    implementation(project(":qc-lock-manager"))
    implementation(project(":qc-external-pg-api"))
    implementation(project(":qc-social-api"))
    implementation(project(":qc-logging"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testImplementation("org.springframework:spring-tx")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:1.1.8")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")

    testImplementation("com.epages:restdocs-api-spec-restassured:0.19.4")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    testImplementation("io.rest-assured:rest-assured:5.5.5")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("software.amazon.awssdk:netty-nio-client:2.31.32")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

sourceSets {
    main {
        resources {
            srcDir("../qc-config")
        }
    }
}

openapi3 {
    setServer("http://localhost:8080")
    title = "Question Cloud API"
    description = "Question Cloud API Documentation"
    version = "1.1.1"
    format = "yaml"
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
