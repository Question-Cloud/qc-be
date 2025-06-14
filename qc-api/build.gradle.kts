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
    }
}

tasks.test {
    useJUnitPlatform()
}