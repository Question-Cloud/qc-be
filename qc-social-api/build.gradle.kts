plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

tasks.getByName<Jar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

kotlin {
    jvmToolchain(17)
}
