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
    
    implementation(project(":qc-core:qc-cart:qc-cart-api"))
    implementation(project(":qc-core:qc-creator:qc-creator-api"))
    implementation(project(":qc-core:qc-pay:qc-pay-api"))
    implementation(project(":qc-core:qc-point:qc-point-api"))
    implementation(project(":qc-core:qc-question:qc-question-api"))
    implementation(project(":qc-core:qc-review:qc-review-api"))
    implementation(project(":qc-core:qc-subscribe:qc-subscribe-api"))
    implementation(project(":qc-core:qc-user:qc-user-api"))
    
    implementation(project(":qc-core:qc-question:qc-question-internal-api"))
    implementation(project(":qc-core:qc-point:qc-point-internal-api"))
    implementation(project(":qc-core:qc-post:qc-post-internal-api"))
    implementation(project(":qc-core:qc-creator:qc-creator-internal-api"))
    implementation(project(":qc-core:qc-user:qc-user-internal-api"))
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testImplementation("org.springframework:spring-tx")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
    testImplementation("org.springframework.security:spring-security-test")
    
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.2")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
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

openapi3 {
    setServer("http://localhost:8080")
    title = "Question Cloud API"
    description = "Question Cloud Unified API Documentation"
    version = "1.1.1"
    format = "yaml"
}

tasks.register<Copy>("collectSnippets") {
    dependsOn(":qc-core:qc-cart:qc-cart-api:test")
    dependsOn(":qc-core:qc-creator:qc-creator-api:test")
    dependsOn(":qc-core:qc-pay:qc-pay-api:test")
    dependsOn(":qc-core:qc-point:qc-point-api:test")
    dependsOn(":qc-core:qc-post:qc-post-api:test")
    dependsOn(":qc-core:qc-question:qc-question-api:test")
    dependsOn(":qc-core:qc-review:qc-review-api:test")
    dependsOn(":qc-core:qc-subscribe:qc-subscribe-api:test")
    dependsOn(":qc-core:qc-user:qc-user-api:test")
    
    from("../qc-core/qc-cart/qc-cart-api/build/generated-snippets")
    from("../qc-core/qc-creator/qc-creator-api/build/generated-snippets")
    from("../qc-core/qc-pay/qc-pay-api/build/generated-snippets")
    from("../qc-core/qc-point/qc-point-api/build/generated-snippets")
    from("../qc-core/qc-post/qc-post-api/build/generated-snippets")
    from("../qc-core/qc-question/qc-question-api/build/generated-snippets")
    from("../qc-core/qc-review/qc-review-api/build/generated-snippets")
    from("../qc-core/qc-subscribe/qc-subscribe-api/build/generated-snippets")
    from("../qc-core/qc-user/qc-user-api/build/generated-snippets")
    
    into("build/generated-snippets")
}