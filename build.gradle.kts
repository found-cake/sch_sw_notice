plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

group = "kr.foundcake"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.seleniumhq.selenium:selenium-java:4.14.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("net.dv8tion:JDA:5.0.0-beta.16") {
        exclude(module="opus-java")
    }
    implementation("com.mysql:mysql-connector-j:8.2.0")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("kr.foundcake.sch_sw_notice.MainKt")
}

tasks {
    shadowJar {
        archiveFileName.set("SchNoticeBot.jar")
    }
}