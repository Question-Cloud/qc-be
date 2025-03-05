plugins {
    id("org.jetbrains.kotlin.jvm")
    kotlin("plugin.spring") version "2.1.10"
    kotlin("plugin.jpa") version "2.1.10"
    kotlin("plugin.allopen") version "2.1.10"
    kotlin("kapt") version "2.1.10"
    kotlin("plugin.lombok") version "2.1.10"
    id("io.freefair.lombok") version "8.10"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    compileOnly("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    implementation(project(":qc-core"))
    implementation(project(":qc-lock-manager"))
    implementation(project(":qc-external-pg-api"))
    implementation(project(":qc-social-api"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testImplementation("org.springframework:spring-tx")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:1.1.8")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
    testImplementation("org.springframework.amqp:spring-rabbit-test:3.2.1")
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

kotlin {
    jvmToolchain(17)
}

kapt {
    keepJavacAnnotationProcessors = true
}