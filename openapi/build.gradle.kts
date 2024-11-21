plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.openapi.generator)
}

val openApiSpecInputSpecPath = "${projectDir}/src/main/resources/openapi.yaml"
val openApiSpecOutputDir = "${layout.buildDirectory.get()}/generated/openapi"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

openApiValidate {
    inputSpec.set(openApiSpecInputSpecPath)
}

openApiGenerate {
    inputSpec.set(openApiSpecInputSpecPath)
    outputDir.set(openApiSpecOutputDir)

    generatorName.set("kotlin")

    globalProperties.set(mapOf(
        "apis" to "",
        "models" to "",
    ))

    configOptions.set(mapOf(
        "library" to "jvm-retrofit2",
        "serializationLibrary" to "kotlinx_serialization",
        "useCoroutines" to "true",
    ))
}

tasks.compileKotlin {
    dependsOn("openApiGenerate")
}
