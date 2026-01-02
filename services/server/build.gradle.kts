plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories { mavenCentral() }

dependencies {
    implementation(project(":proto"))
    implementation(libs.guava)
    implementation(libs.grpc.netty)
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
