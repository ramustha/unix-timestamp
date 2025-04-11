plugins {
    id("java")
    // https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
    id("org.jetbrains.intellij.platform") version "2.5.0"
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
}

group = "com.ramusthastudio.plugin"
version = "7.1.1"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    intellijPlatform {
        create("IU", "2024.1")

        bundledPlugin("com.intellij.java")
        bundledPlugin("org.intellij.groovy")
        bundledPlugin("com.intellij.css")
        bundledPlugin("com.intellij.database")
        bundledPlugin("org.jetbrains.kotlin")
        bundledPlugin("JavaScript")

        // https://plugins.jetbrains.com/plugin/13121-http-client/versions/stable
        plugin("com.jetbrains.restClient", "241.14494.150")
        // https://plugins.jetbrains.com/plugin/631-python/versions/stable
        plugin("Pythonid", "241.14494.158")
        // https://plugins.jetbrains.com/plugin/9568-go/versions/stable
        plugin("org.jetbrains.plugins.go", "241.14494.240")
        // https://plugins.jetbrains.com/plugin/6610-php/versions/stable
        plugin("com.jetbrains.php", "241.14494.240")
        // https://plugins.jetbrains.com/plugin/1347-scala/versions
        plugin("org.intellij.scala", "2024.1.20")
    }

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-framework-engine:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.7.2")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "231"
            untilBuild = "251.*"
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
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    compileTestKotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    test {
        useJUnitPlatform()
    }
}
