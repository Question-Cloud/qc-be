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
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.0")

}

tasks.test {
    useJUnitPlatform()
}