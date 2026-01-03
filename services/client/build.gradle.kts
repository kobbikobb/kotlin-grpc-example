plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories { mavenCentral() }

dependencies {
    implementation(project(":proto"))
    implementation(libs.grpc.netty)
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.kotlinx.coroutines.core)
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
    mainClass = "org.example.client.MainKt"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
