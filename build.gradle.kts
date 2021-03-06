import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    kotlin("kapt") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("plugin.jpa") version "1.5.21"
    jacoco
    id("com.github.nbaztec.coveralls-jacoco") version "1.2.13"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "at.bayava"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
val queryDslVersion = "5.0.0"
val kotestVersion = "4.6.+"
val springBootVersion = "2.5.4"

repositories {
    mavenCentral()
}

dependencies {
    kapt("com.querydsl:querydsl-apt:${queryDslVersion}:jpa")
    kaptTest("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("net.jazdw:rql-parser:0.3.2")
    implementation("com.querydsl:querydsl-core:${queryDslVersion}")
    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}")
    implementation("com.querydsl:querydsl-apt:${queryDslVersion}:jpa")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.+")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.+")
    compileOnly("jakarta.persistence:jakarta.persistence-api:2.2.3")

    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("io.kotest:kotest-runner-junit5:${kotestVersion}")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.0.+")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}