plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf)
}

repositories { mavenCentral() }

dependencies {
    api(libs.grpc.kotlin.stub)
    api(libs.grpc.protobuf)
    api(libs.protobuf.kotlin)
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:3.25.1" }
    plugins {
        create("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:1.60.1" }
        create("grpckt") { artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar" }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins { create("kotlin") }
        }
    }
}
