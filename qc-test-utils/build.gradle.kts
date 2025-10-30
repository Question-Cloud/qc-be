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
    
    implementation(project(":qc-common"))
    implementation(project(":qc-infra::qc-rdb"))
    
    api("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
    
    testImplementation(kotlin("test"))
}