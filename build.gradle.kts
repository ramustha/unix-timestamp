plugins {
    id("java")
    // https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
    id("org.jetbrains.intellij") version "1.17.4"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

group = "com.ramusthastudio.plugin"
version = "6.5.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven("https://www.jetbrains.com/intellij-repository/snapshots")
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-framework-engine:5.6.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    // https://www.jetbrains.com/idea/download/other.html
    version.set("2024.1")
    type.set("IU") // Target IDE Platform
    updateSinceUntilBuild.set(false)

    // https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#intellij-extension-plugins
    plugins.set(listOf(
        "com.intellij.java",
        "org.intellij.groovy",
        "com.intellij.css",
        "com.intellij.database",
        "org.jetbrains.kotlin",
        "JavaScript",
        // https://plugins.jetbrains.com/plugin/9442-vue-js/versions/stable
        "org.jetbrains.plugins.vue:241.14494.238",
        // https://plugins.jetbrains.com/plugin/13121-http-client/versions/stable
        "com.jetbrains.restClient:241.14494.150",
        // https://plugins.jetbrains.com/plugin/631-python/versions/stable
        "Pythonid:241.14494.314",
        // https://plugins.jetbrains.com/plugin/9568-go/versions/stable
        "org.jetbrains.plugins.go:241.14494.127",
        // https://plugins.jetbrains.com/plugin/6610-php/versions/stable
        "com.jetbrains.php:241.14494.240",
        // https://plugins.jetbrains.com/plugin/1347-scala/versions
        "org.intellij.scala:2024.1.24",
    ))
}

tasks {
    // Set the JVM compatibility versions
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    // https://plugins.jetbrains.com/docs/marketplace/product-versions-in-use-statistics.html
    patchPluginXml {
         sinceBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runPluginVerifier {
        ideVersions.set(
            listOf(
                "IU-2024.1"
            ))
    }

    test {
        useJUnitPlatform()
    }

}
