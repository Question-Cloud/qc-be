import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-web:6.1.14")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

kotlin {
    jvmToolchain(17)
}
