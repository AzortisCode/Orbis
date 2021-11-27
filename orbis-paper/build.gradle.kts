import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.papermc.paperweight.userdev") version "1.1.15-SNAPSHOT"
}

group = "com.azortis"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
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
    paperDevBundle("1.17.1-R0.1-SNAPSHOT")
    compileOnly("org.slf4j:slf4j-api:1.7.31")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("Orbis")
        mergeServiceFiles()
    }
}

tasks {
    build {
        dependsOn(shadowJar)
        dependsOn(reobfJar)
    }
}
