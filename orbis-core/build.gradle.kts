plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.azortis"
version = "1.0-SNAPSHOT"

sourceSets.main {
    java.setSrcDirs(listOf("src/main/java","src/generated/java"))
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
    // No adapter contains this by default
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("net.lingala.zip4j:zip4j:2.7.0")
    implementation("org.projectlombok:lombok:1.18.20")

    annotationProcessor("org.projectlombok:lombok:1.18.20")

    // Adapters should determine if it should be added to a shadow jar
    compileOnly("io.github.jglrxavpok.hephaistos:common:2.2.0")
    compileOnly("it.unimi.dsi:fastutil:8.5.6")
    compileOnly("com.google.code.gson:gson:2.8.9")
    compileOnly("org.slf4j:slf4j-api:1.7.31")
    compileOnly("commons-io:commons-io:2.8.0")
    compileOnly("org.jetbrains:annotations:16.0.1")
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
    build {
        dependsOn(shadowJar)
    }
}
