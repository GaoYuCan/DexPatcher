plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "site.gaoyucan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.smali:dexlib2:2.5.2")
    implementation("com.google.guava:guava:32.1.3-jre")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}