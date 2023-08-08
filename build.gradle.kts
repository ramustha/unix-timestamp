plugins {
    id("java")
    // https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
    id("org.jetbrains.intellij") version "1.15.0"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

group = "com.ramusthastudio.plugin"
version = "5.0.1"

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
    version.set("2022.3")
    type.set("IU") // Target IDE Platform
    updateSinceUntilBuild.set(false)

    plugins.set(listOf(
        "com.intellij.java",
        "com.intellij.css",
        "com.intellij.database",
        "org.jetbrains.kotlin",
        "JavaScript",
        "org.jetbrains.plugins.vue:223.7571.233",
        "com.jetbrains.restClient:223.7571.59"
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
        sinceBuild.set("221.*")
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
                "IU-2023.1"
            ))
    }

    test {
        useJUnitPlatform()
    }

}
