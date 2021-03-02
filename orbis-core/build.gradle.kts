plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.azortis"
version = "1.0-SNAPSHOT"

repositories {
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

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("com.github.Minestom:Minestom:3026e46220")
    implementation("net.sf.trove4j:trove4j:3.0.3")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}