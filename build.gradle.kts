import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "v.0.0.1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //core
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("de.vandermeer:asciitable:0.3.2")
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}