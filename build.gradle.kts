plugins {
    id("java")
    // https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
    id("org.jetbrains.intellij") version "1.14.1"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

group = "com.ramusthastudio.plugin"
version = "4.2.0"

repositories {
    mavenCentral()
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven("https://www.jetbrains.com/intellij-repository/snapshots")
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("junit:junit:4.13.2")
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

}
