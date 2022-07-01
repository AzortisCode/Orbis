/*
 * A dynamic data-driven world generator plugin/library for Minecraft servers.
 *     Copyright (C) 2022 Azortis
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
    implementation("net.kyori:adventure-api:4.9.3")
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