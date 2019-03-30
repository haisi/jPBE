plugins {
    `java-library`
}

repositories {
    jcenter()
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
//    api("org.apache.commons:commons-math3:3.6.1")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("ch.qos.logback:logback-classic:1.2.3")

    // Use JUnit test framework
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.1")
    testImplementation("org.assertj:assertj-core:3.11.1")
}
