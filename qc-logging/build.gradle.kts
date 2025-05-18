plugins {
    kotlin("jvm")
}

group = "com.eager"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-context")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}