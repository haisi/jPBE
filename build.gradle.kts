import net.ltgt.gradle.errorprone.errorprone

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
    implementation("com.google.guava:guava:30.1-jre")
    val log4jVersion = "2.14.0"
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    // Use JUnit Jupiter API for testing.
    val junitVersion = "5.7.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation("org.mockito:mockito-core:3.8.0")
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone.disable("BracesRequired", "MissingSummary", "EqualsGetClass", "OptionalOrElseMethodInvocation",
        "PreferSafeLoggableExceptions", "PreferSafeLoggingPreconditions",
        "StrictUnusedVariable" // Re-enable in the future
    )
}

tasks.named<JavaCompile>("compileTestJava") {
    options.errorprone.isEnabled.set(false)
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
