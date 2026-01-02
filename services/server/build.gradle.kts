plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories { mavenCentral() }

dependencies {
    implementation(project(":proto"))
    implementation(libs.guava)
    implementation(libs.grpc.netty)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

testing {
    suites {
        val test by
            getting(JvmTestSuite::class) {
                useJUnitJupiter("5.11.3")
            }
    }
}

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

application {
    mainClass = "org.example.AppKt"
}
