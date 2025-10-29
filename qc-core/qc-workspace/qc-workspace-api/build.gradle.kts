plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    
    compileOnly("org.springframework:spring-tx")
    
    implementation(project(":qc-common"))
    
    implementation(project(":qc-internal-api-interface:qc-user-internal-api-interface"))
    implementation(project(":qc-internal-api-interface:qc-question-internal-api-interface"))
    implementation(project(":qc-internal-api-interface:qc-post-internal-api-interface"))
    implementation(project(":qc-internal-api-interface:qc-creator-internal-api-interface"))
    
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}