import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
    jacoco
}

group = "ru.dm"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("io.github.microutils:kotlin-logging:2.0.11")

    implementation("net.logstash.logback:logstash-logback-encoder:6.6")

    // detekt
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.17.1")

    // telegram
    implementation("org.telegram:telegrambots-spring-boot-starter:4.9.2")
    implementation("org.telegram:telegrambotsextensions:4.9.2")
    implementation("org.telegram:telegrambots-meta:4.9.2")

    // google api
    implementation("com.google.auth:google-auth-library-credentials:0.20.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:0.20.0")
    implementation("com.google.apis:google-api-services-sheets:v4-rev20200225-1.30.9")

    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql:42.2.23")

    // postgresql container
    testImplementation("org.testcontainers:postgresql")

    // tests
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(
            group = "org.junit.vintage",
            module = "junit-vintage-engine"
        )
    }
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:1.15.2")
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

detekt {
    config = files("${projectDir.toPath()}/config/detekt/detekt.yml")
    autoCorrect = true
    toolVersion = "1.17.1"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
