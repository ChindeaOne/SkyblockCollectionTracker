import org.apache.commons.lang3.SystemUtils

plugins {
    idea
    java
    id("gg.essential.loom") version "0.10.0.+" // Use the latest Loom version
    id("dev.architectury.architectury-pack200") version "0.1.3" // Architectury Pack200 support
    id("com.github.johnrengelman.shadow") version "8.1.1" // Shadow plugin for fat jars
    kotlin("jvm") version "1.9.0" // Kotlin support
}

// Constants
val baseGroup: String by project
val mcVersion: String by project
val version: String by project
val modid: String by project
val transformerFile = file("src/main/resources/accesstransformer.cfg")

// Toolchains
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8)) // Specify Java version
}

// Minecraft configuration
loom {
    log4jConfigs.from(file("log4j2.xml")) // Log4j config
    launchConfigs {
        "client" {
            arg("--tweakClass", "io.github.notenoughupdates.moulconfig.tweaker.DevelopmentResourceTweaker") // Tweak class for NotEnoughUpdates
        }
    }
    runConfigs {
        "client" {
            if (SystemUtils.IS_OS_MAC_OSX) {
                vmArgs.remove("-XstartOnFirstThread") // Prevent crash on macOS
            }
        }
        remove(getByName("server")) // Remove server run configuration
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter()) // Forge pack200 provider
    }
}

sourceSets.main {
    output.setResourcesDir(sourceSets.main.flatMap { it.java.classesDirectory }) // Set resources directory
}

// Repositories
repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/maven/") // SpongePowered repository
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") // DevAuth
    maven("https://repo.nea.moe/releases") // libautoupdate
    maven("https://maven.notenoughupdates.org/releases/") // NotEnoughUpdates
    maven("https://jitpack.io"){ // NotEnoughUpdates
        content {
            includeGroupByRegex("(com|io)\\.github\\..*")
        }
    }
}

// Shadow configurations
val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this) // Shadow implementation
}

val shadowModImpl: Configuration by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this) // Shadow mod implementation
}

// Dependencies
dependencies {
    minecraft("com.mojang:minecraft:1.8.9") // Minecraft dependency
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9") // MCP mappings
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9") // Forge dependency

    implementation(kotlin("stdlib-jdk8")) // Kotlin standard library
    shadowImpl("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") {
        exclude(group = "org.jetbrains.kotlin") // Exclude conflicting Kotlin version
    }

    shadowModImpl(libs.moulconfig)
    shadowImpl(libs.libautoupdate) {
        exclude(module = "gson")
    }
    shadowImpl("org.jetbrains.kotlin:kotlin-reflect:1.9.0") // Kotlin reflection

    runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.2.1") // Runtime dependency for DevAuth
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0" // Set Kotlin language version
            enableLanguageFeature("BreakContinueInInlineLambdas") // Enable inline lambdas
        }
    }
}

// Tasks
tasks.compileJava {
    dependsOn(tasks.processResources) // Ensure resources are processed before compilation
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8" // Set UTF-8 encoding for Java files
}

tasks.withType(org.gradle.jvm.tasks.Jar::class) {
    archiveBaseName.set(modid) // Set the base name for the jar
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true" // Mark as a Forge mod
        this["ForceLoadAsMod"] = "true" // Force load the mod
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mcversion", mcVersion)
    inputs.property("modid", modid)
    inputs.property("basePackage", baseGroup)

    filesMatching(listOf("mcmod.info", "mixins.$modid.json")) {
        expand(inputs.properties) // Expand properties in resource files
    }

    rename("accesstransformer.cfg", "META-INF/${modid}_at.cfg") // Rename access transformer file
}

// Shadow and relocation configuration
val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("") // Set classifier for remapped jar
    from(tasks.shadowJar) // From shadow jar
    input.set(tasks.shadowJar.get().archiveFile) // Input shadow jar
}

tasks.jar {
    archiveClassifier.set("without-deps") // Set classifier for jar without dependencies
    destinationDirectory.set(layout.buildDirectory.dir("intermediates")) // Set destination for jar
}

tasks.shadowJar {
    destinationDirectory.set(layout.buildDirectory.dir("intermediates")) // Set destination for shadow jar
    archiveClassifier.set("non-obfuscated-with-deps") // Classifier for shadow jar
    configurations = listOf(shadowModImpl, shadowImpl)

    doLast {
        configurations.forEach {
            println("Copying dependencies into mod: ${it.files}") // Print copied dependencies
        }
    }
    exclude("META-INF/versions/**") // Exclude specific files

    relocate("io.github.notenoughupdates.moulconfig","io.github.chindeaytb.collectiontracker.deps.moulconfig") // Relocate MoulConfig
    relocate("moe.nea.libautoupdate", "io.github.chindeaytb.collectiontracker.deps.libautoupdate") // Relocate libautoupdate
}

tasks.assemble.get().dependsOn(tasks.remapJar) // Ensure remap jar task runs during assemble
