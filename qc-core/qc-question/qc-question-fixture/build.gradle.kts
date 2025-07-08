plugins {
    kotlin("jvm")
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":qc-core:qc-question:qc-question-core"))
    
    implementation("com.navercorp.fixturemonkey:fixture-monkey-starter:1.1.8")
    implementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
    
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}