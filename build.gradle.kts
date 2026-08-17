plugins {
    id("java")
    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
    id("org.jetbrains.intellij.platform") version "2.18.1"
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
}

group = "com.ramusthastudio.plugin"
version = "8.0.0"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

java {
    // IntelliJ Platform 2024.2+ runs on Java 21.
    sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
    intellijPlatform {
        // Build against Java 21-based 2026.1 so the same artifact remains compatible
        // with 2024.2+ and also runs on the Java 25-based 2026.2 platform.
        intellijIdea("2026.1.5")

        bundledPlugin("com.intellij.java")
        bundledPlugin("org.intellij.groovy")
        bundledPlugin("com.intellij.css")
        bundledPlugin("com.intellij.database")
        bundledPlugin("org.jetbrains.kotlin")
        bundledPlugin("JavaScript")
        // Required for 2024.3+
        bundledPlugin("com.intellij.modules.json")

        // https://plugins.jetbrains.com/plugin/13121-http-client/versions/stable
        plugin("com.jetbrains.restClient", "261.24374.34")
        // https://plugins.jetbrains.com/plugin/631-python/versions/stable
        plugin("Pythonid", "261.26222.65")
        // https://plugins.jetbrains.com/plugin/7322-python-community-edition/versions
        plugin("PythonCore", "261.26222.65")
        // https://plugins.jetbrains.com/plugin/9568-go/versions/stable
        plugin("org.jetbrains.plugins.go", "261.26222.22")
        // https://plugins.jetbrains.com/plugin/6610-php/versions/stable
        plugin("com.jetbrains.php", "261.26222.22")
        // https://plugins.jetbrains.com/plugin/1347-scala/versions
        plugin("org.intellij.scala", "2026.1.20")
    }

    testImplementation(kotlin("test-junit5"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.kotest:kotest-framework-engine:6.1.11")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:6.1.11")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            // Java 21 bytecode supports IntelliJ Platform 2024.2+.
            sinceBuild = "242"
        }
    }

    publishing {
        token = System.getenv("PUBLISH_TOKEN")
    }

    signing {
        certificateChain = System.getenv("CERTIFICATE_CHAIN")
        privateKey = System.getenv("PRIVATE_KEY")
        password = System.getenv("PRIVATE_KEY_PASSWORD")
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    compileKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    compileTestKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    test {
        useJUnitPlatform()
    }
}
