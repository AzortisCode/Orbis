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
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.azortis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        url = uri("https://repo.spongepowered.org/maven")
    }

    maven {
        url = uri("https://libraries.minecraft.net")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(project(":orbis-core"))
    implementation("com.github.Minestom:Minestom:3026e46220")
    implementation("org.projectlombok:lombok:1.18.20")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

