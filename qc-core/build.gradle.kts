plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
    kotlin("plugin.jpa") version "2.1.10"
    kotlin("plugin.allopen") version "2.1.10"
    kotlin("kapt") version "2.1.10"
    kotlin("plugin.lombok") version "2.1.10"
    id("io.freefair.lombok") version "8.10"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

dependencies {
    implementation(kotlin("reflect"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework.security:spring-security-crypto:6.3.4")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    compileOnly("org.springframework:spring-tx")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")

    implementation(project(":qc-lock-manager"))
    
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
}

val querydslDir = file("src/main/generated")

sourceSets {
    main {
        kotlin.srcDirs += querydslDir
        resources.srcDir("../qc-config")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.generatedSourceOutputDirectory.set(file(querydslDir))
}

tasks.clean {
    doLast {
        file(querydslDir).deleteRecursively()
    }
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}

kotlin {
    jvmToolchain(17)
}

kapt {
    keepJavacAnnotationProcessors = true
}