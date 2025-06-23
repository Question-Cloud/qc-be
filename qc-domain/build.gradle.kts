plugins {
    kotlin("jvm")
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
}

subprojects {
    dependencies {
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")

        testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.2")
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
        testImplementation(project(":qc-api-container"))
    }
}

tasks.test {
    useJUnitPlatform()
}