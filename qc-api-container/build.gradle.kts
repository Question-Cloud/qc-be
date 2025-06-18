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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation(platform("io.micrometer:micrometer-tracing-bom:latest.release"))
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    implementation(project(":qc-common"))
    implementation(project(":qc-logging"))
    implementation(project(":qc-event"))

    implementation(project(":qc-domain:qc-cart:qc-cart-api"))
    implementation(project(":qc-domain:qc-coupon:qc-coupon-api"))
    implementation(project(":qc-domain:qc-creator:qc-creator-api"))
    implementation(project(":qc-domain:qc-pay:qc-pay-api"))
    implementation(project(":qc-domain:qc-point:qc-point-api"))
    implementation(project(":qc-domain:qc-question:qc-question-api"))
    implementation(project(":qc-domain:qc-review:qc-review-api"))
    implementation(project(":qc-domain:qc-subscribe:qc-subscribe-api"))
    implementation(project(":qc-domain:qc-user:qc-user-api"))

    implementation(project(":qc-domain:qc-question:qc-question-internal-api"))
    implementation(project(":qc-domain:qc-point:qc-point-internal-api"))
    implementation(project(":qc-domain:qc-post:qc-post-internal-api"))
    implementation(project(":qc-domain:qc-creator:qc-creator-internal-api"))
    implementation(project(":qc-domain:qc-user:qc-user-internal-api"))
}

sourceSets {
    main {
        resources {
            srcDir("../qc-config")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
