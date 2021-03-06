plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.azortis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
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

