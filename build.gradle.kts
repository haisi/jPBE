buildscript {
    repositories {
        gradlePluginPortal()
        maven { setUrl("http://palantir.bintray.com/releases") }
    }
    dependencies {
        classpath("com.palantir.baseline:gradle-baseline-java:3.69.0")
        classpath("gradle.plugin.org.inferred:gradle-processors:3.3.0")
    }
}

repositories {
    mavenCentral()
    maven { setUrl("http://palantir.bintray.com/releases") }
}

plugins {
    `java-library`
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
