plugins {
    kotlin("jvm")
}

group = "com.eager.questioncloud"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    subprojects {
        sourceSets {
            main {
                resources {
                    srcDir("../qc-config")
                }
            }
        }
    }
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}