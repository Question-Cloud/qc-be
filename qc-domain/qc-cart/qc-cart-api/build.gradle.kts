plugins {
    kotlin("jvm")
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.springframework:spring-tx")

    implementation(project(":qc-common"))
    implementation(project(":qc-domain:qc-cart:qc-cart-core"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}