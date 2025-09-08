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
        
        testImplementation("org.apache.commons:commons-lang3:3.17.0")
        
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:1.1.8")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
        
        testImplementation(project(":qc-api-container"))
        testImplementation(project(":qc-test-utils"))
    }
}

tasks.test {
    useJUnitPlatform()
}