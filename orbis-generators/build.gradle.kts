plugins {
    java
    application
}

group = "com.azortis"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.azortis.orbis.codegen.Generators")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(project(":orbis-core"))
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.jetbrains:annotations:16.0.1")
    implementation("com.squareup:javapoet:1.13.0")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    run {
        run.get().setArgsString(
            "$" + project.rootProject.childProjects["orbis-core"]!!.projectDir.invariantSeparatorsPath +
                    "/src/generated/java$"
        )
    }
}