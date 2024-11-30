plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.jsinco.discord"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
}

dependencies {
    // JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.24") {
        exclude("org.slf4j", "slf4j-api")
    }

    // Framework
    implementation("com.github.Jsinco:jda-framework:1.6")

    // Logger
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.24.1")
    implementation("org.apache.logging.log4j:log4j-api:2.24.1")

    // Google guava/gson
    implementation("com.google.guava:guava:33.3.1-jre")
    implementation("com.google.code.gson:gson:2.10.1")

    // Config
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.5")
    //implementation("eu.okaeri:okaeri-configs-json-gson:5.0.5")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}

tasks {

    build {
        dependsOn(shadowJar)
    }

    jar {
        enabled = false
    }

    shadowJar {
        manifest {
            attributes(
                "Main-Class" to "dev.jsinco.discord.Main"
            )
        }
        dependencies {

        }
        archiveBaseName.set(project.rootProject.name)
        archiveClassifier.set("")
    }
}