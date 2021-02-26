buildscript {
    repositories {
        gradlePluginPortal()
        maven { setUrl("http://palantir.bintray.com/releases") }
    }
}

repositories {
    mavenCentral()
    maven { setUrl("http://palantir.bintray.com/releases") }
}

plugins {
    `java-library`
    // Part of palantir baseline sanity checks.
    // Processor for Google's "Error Prone" tool.
    // However, for some reason this breaks the build.
//    id("org.inferred.processors") version "3.3.0"
    id("com.palantir.baseline") version "3.69.0"
    id("org.asciidoctor.convert") version "1.5.9.2"
}

dependencies {
    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:28.1-jre")

    // Use JUnit Jupiter API for testing.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testImplementation("org.assertj:assertj-core:3.15.0")
    testImplementation("org.mockito:mockito-core:3.2.4")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
