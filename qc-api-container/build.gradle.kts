plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "2.1.10"
    id("com.epages.restdocs-api-spec") version "0.19.2"
    id("io.sentry.jvm.gradle") version "5.12.2"
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    compileOnly("org.springframework:spring-tx")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.retry:spring-retry")
    
    implementation(project(":qc-core:qc-cart:qc-cart-api"))
    implementation(project(":qc-core:qc-creator:qc-creator-api"))
    implementation(project(":qc-core:qc-pay:qc-pay-api"))
    implementation(project(":qc-core:qc-point:qc-point-api"))
    implementation(project(":qc-core:qc-post:qc-post-api"))
    implementation(project(":qc-core:qc-question:qc-question-api"))
    implementation(project(":qc-core:qc-review:qc-review-api"))
    implementation(project(":qc-core:qc-subscribe:qc-subscribe-api"))
    implementation(project(":qc-core:qc-user:qc-user-api"))
    implementation(project(":qc-core:qc-workspace:qc-workspace-api"))
    
    implementation(project(":qc-core:qc-question:qc-question-internal-api"))
    implementation(project(":qc-core:qc-point:qc-point-internal-api"))
    implementation(project(":qc-core:qc-post:qc-post-internal-api"))
    implementation(project(":qc-core:qc-creator:qc-creator-internal-api"))
    implementation(project(":qc-core:qc-user:qc-user-internal-api"))
    
    implementation(project(":qc-infra:qc-rdb"))
    implementation(project(":qc-infra:qc-redis"))
    implementation(project(":qc-infra:qc-mail"))
    implementation(project(":qc-infra:qc-event"))
    
    implementation(project(":qc-common"))
    implementation(project(":qc-logging"))
    
    implementation(project(":qc-test-utils"))
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-tx")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter-kotlin:1.1.9")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.2")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
}

sentry {
    includeSourceContext = true
    org = "questioncloud"
    projectName = "questioncloud"
    authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

sourceSets {
    main {
        resources {
            srcDir("../qc-config")
        }
    }
}

openapi3 {
    setServer("https://questioncloud.store")
    title = "Question Cloud API"
    description = "Question Cloud Unified API Documentation"
    version = "1.1.1"
    format = "yaml"
}

val modules = listOf(
    "qc-cart",
    "qc-creator",
    "qc-pay",
    "qc-point",
    "qc-post",
    "qc-question",
    "qc-review",
    "qc-subscribe",
    "qc-user",
    "qc-workspace"
)

val documentTests = modules.map { moduleName ->
    tasks.register<Test>("${moduleName}DocumentTest") {
        val apiProject = project(":qc-core:${moduleName}:${moduleName}-api")
        testClassesDirs = apiProject.sourceSets.test.get().output.classesDirs
        classpath = apiProject.sourceSets.test.get().runtimeClasspath
        useJUnitPlatform()
        include("**/*Document.class")
    }
}

tasks.register<Task>("collectSnippets") {
    documentTests.forEach {
        dependsOn(it)
    }
}

tasks.register<Copy>("generateSwagger") {
    dependsOn("openapi3")
    delete("src/main/resources/static/openapi3.yaml")
    from("$buildDir/api-spec/openapi3.yaml")
    into("src/main/resources/static")
}

tasks.test {
    minHeapSize = "2g"
}