plugins {
    kotlin("jvm")
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

subprojects {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-aop")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        compileOnly("org.springframework:spring-tx")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
        testImplementation("org.springframework:spring-tx")
        testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
        testImplementation("org.springframework.security:spring-security-test")

        testImplementation(project(":qc-api:qc-api-container"))
        testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.2")
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    }

    sourceSets {
        main {
            resources {
                srcDir("../../qc-config")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}