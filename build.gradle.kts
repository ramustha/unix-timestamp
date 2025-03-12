import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("java")
    // https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
    id("org.jetbrains.intellij.platform") version "2.2.1"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

group = "com.ramusthastudio.plugin"
version = "7.0.0"

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
        localPlatformArtifacts()
        releases()
        marketplace()
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

        // https://plugins.jetbrains.com/plugin/9442-vue-js/versions/stable
        plugin("org.jetbrains.plugins.vue", "241.14494.238")
        // https://plugins.jetbrains.com/plugin/13121-http-client/versions/stable
        plugin("com.jetbrains.restClient", "241.14494.150")
        // https://plugins.jetbrains.com/plugin/631-python/versions/stable
        plugin("Pythonid", "241.14494.314")
        // https://plugins.jetbrains.com/plugin/9568-go/versions/stable
        plugin("org.jetbrains.plugins.go", "241.14494.127")
        // https://plugins.jetbrains.com/plugin/6610-php/versions/stable
        plugin("com.jetbrains.php", "241.14494.237")
        // https://plugins.jetbrains.com/plugin/1347-scala/versions
        plugin("org.intellij.scala", "2024.1.24")
    }

    testImplementation(kotlin("test-junit5"))
    testImplementation("io.kotest:kotest-framework-engine:5.6.2")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.6.2")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "231"
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
            ide(IntelliJPlatformType.IntellijIdeaUltimate, "2024.1")
            recommended()
            select {
                types = listOf(IntelliJPlatformType.IntellijIdeaUltimate)
                sinceBuild = "231"
            }
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    test {
        useJUnitPlatform()
    }

}
