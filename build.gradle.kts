import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    kotlin("kapt") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
    kotlin("plugin.jpa") version "1.5.21"
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "at.bayava"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
val queryDslVersion="5.0.0"

repositories {
    mavenCentral()
}

dependencies {
    kapt("com.querydsl:querydsl-apt:${queryDslVersion}:jpa")
    kaptTest("org.springframework.boot:spring-boot-starter-data-jpa:2.1.1.RELEASE")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("cz.jirutka.rsql:rsql-parser:2.1.0")
    implementation("com.querydsl:querydsl-core:${queryDslVersion}")
    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}")
    implementation("com.querydsl:querydsl-apt:${queryDslVersion}:jpa")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.+")
    implementation("ch.qos.logback:logback-classic:1.2.+")

//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.+")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
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
