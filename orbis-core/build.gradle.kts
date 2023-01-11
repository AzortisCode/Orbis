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
    `maven-publish`
}

group = "com.azortis"
version = "0.3-ALPHA"

sourceSets.main {
    java.setSrcDirs(listOf("src/main/java", "src/generated/java"))
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo.spongepowered.org/maven")
    }

    maven {
        url = uri("https://libraries.minecraft.net")
    }

    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    // Libraries downloaded in core package
    compileOnly("com.google.guava:guava:31.0.1-jre")
    compileOnly("net.lingala.zip4j:zip4j:2.10.0")
    compileOnly("org.projectlombok:lombok:1.18.24")
    compileOnly("cloud.commandframework:cloud-core:1.8.0")
    compileOnly("cloud.commandframework:cloud-annotations:1.8.0")

    annotationProcessor("org.projectlombok:lombok:1.18.20")
    annotationProcessor("cloud.commandframework:cloud-annotations:1.7.1")

    // Adapters should determine if libraries should be downloaded, or if the platform already has them provided
    compileOnly("net.kyori:adventure-api:4.12.0")
    compileOnly("net.kyori:adventure-nbt:4.12.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.12.0")
    compileOnly("it.unimi.dsi:fastutil:8.5.6")
    compileOnly("com.google.code.gson:gson:2.10")
    compileOnly("org.slf4j:slf4j-api:1.7.31")
    compileOnly("commons-io:commons-io:2.8.0")
    compileOnly("org.jetbrains:annotations:23.1.0")
    compileOnly("org.apache.commons:commons-lang3:3.12.0")


    // For testing
    testImplementation("net.kyori:adventure-api:4.9.3")
    testImplementation("it.unimi.dsi:fastutil:8.5.6")
    testImplementation("org.slf4j:slf4j-api:1.7.31")
    testImplementation("org.jetbrains:annotations:16.0.1")
    testImplementation("com.google.code.gson:gson:2.8.9")
    testImplementation("commons-io:commons-io:2.8.0")
    testImplementation("cloud.commandframework:cloud-core:1.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.apache.logging.log4j:log4j-core:2.14.1")
    testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
    testImplementation("org.apache.commons:commons-lang3:3.12.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.azortis"
            artifactId = "orbis"
            version = version

            from(components["java"])
        }
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    test {
        useJUnitPlatform()
    }
}
