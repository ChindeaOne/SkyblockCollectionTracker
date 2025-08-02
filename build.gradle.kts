import org.apache.commons.lang3.SystemUtils
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import sct.setVersionFromGit

plugins {
    idea
    java
    id("gg.essential.loom")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm")
    id("net.kyori.blossom")
}

version = setVersionFromGit()

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    toolchain.vendor.set(JvmVendorSpec.ADOPTIUM)
}

loom {
    runs {
        named("client") {
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
            programArgs("--tweakClass", "org.spongepowered.asm.launch.MixinTweaker")
        }
    }
    log4jConfigs.from(file("log4j2.xml"))
    runConfigs {
        "client" {
            if (SystemUtils.IS_OS_MAC_OSX) {
                vmArgs.remove("-XstartOnFirstThread")
            }
        }
        remove(getByName("server"))
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig("mixins.sct.json")
    }
    @Suppress("UnstableApiUsage") mixin {
        defaultRefmapName.set("mixins.sct.refmap.json")
    }
}

// Repositories
repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.spongepowered.org/maven/")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    maven("https://repo.nea.moe/releases")
    maven("https://maven.notenoughupdates.org/releases/")
    maven("https://jitpack.io") {
        content {
            includeGroupByRegex("(com|io)\\.github\\..*")
        }
    }
    maven("https://maven.minecraftforge.net") {
        metadataSources {
            artifact()
        }
    }
}
sourceSets.main {
    resources.destinationDirectory.set(kotlin.destinationDirectory)
    output.setResourcesDir(sourceSets.main.flatMap { it.java.classesDirectory })
}

val kotlinDependencies: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

// Shadow configurations
val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val shadowModImpl: Configuration by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)
}

// Dependencies
dependencies {
    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    implementation(kotlin("stdlib-jdk8"))
    shadowImpl("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") {
        exclude(group = "org.jetbrains.kotlin")
    }

    shadowModImpl(libs.moulconfig)
    shadowImpl(libs.modrinthautoupdater) {
        exclude(group = "gson")
    }

    shadowImpl("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

    runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.2.1")

    kotlinDependencies(kotlin("stdlib"))
    kotlinDependencies(kotlin("reflect"))

    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    }

    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT")

    annotationProcessor("net.fabricmc:sponge-mixin:0.11.4+mixin.0.8.5")
    testAnnotationProcessor("net.fabricmc:sponge-mixin:0.11.4+mixin.0.8.5")

}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
            enableLanguageFeature("BreakContinueInInlineLambdas")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val generateVersionConstants by tasks.registering {
    val outputDir = file("src/main/java/io/github/chindeaytb/collectiontracker/util")
    val outputFile = file("$outputDir/VersionConstants.kt")
    inputs.property("modVersion", project.version)
    outputs.file(outputFile)

    doLast {
        outputDir.mkdirs()
        outputFile.writeText(
            """
            package io.github.chindeaytb.collectiontracker.util

            object VersionConstants {
                const val MOD_VERSION: String = "${project.version}"
            }
            """.trimIndent()
        )
    }
}

tasks.named("compileKotlin") {
    dependsOn(generateVersionConstants)
}
tasks.named("compileJava") {
    dependsOn(generateVersionConstants)
}

val cleanVersionConstants by tasks.registering(Delete::class) {
    delete("src/main/java/io/github/chindeaytb/collectiontracker/util/VersionConstants.kt")
}

// Tasks
tasks.compileJava {
    dependsOn(tasks.processResources)
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.withType(org.gradle.jvm.tasks.Jar::class) {
    archiveBaseName.set("SkyblockCollectionTracker")
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"
        this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        this["MixinConfigs"] = "mixins.sct.json"
    }
}

tasks.processResources {
    inputs.property("version", version)
    filesMatching(listOf("mcmod.info", "mixins.sct.json")) {
        expand("version" to version)
    }
    filesMatching("urls.properties") {
        expand(
            "TOKEN_URL" to (System.getenv("TOKEN_URL") ?: ""),
            "TRACKED_COLLECTION_URL" to (System.getenv("TRACKED_COLLECTION_URL") ?: ""),
            "AVAILABLE_COLLECTIONS_URL" to (System.getenv("AVAILABLE_COLLECTIONS_URL") ?: ""),
            "AVAILABLE_GEMSTONES_URL" to (System.getenv("AVAILABLE_GEMSTONES_URL") ?: ""),
            "NPC_PRICES_URL" to (System.getenv("NPC_PRICES_URL") ?: ""),
            "BAZAAR_URL" to (System.getenv("BAZAAR_URL") ?: ""),
            "STATUS_URL" to (System.getenv("STATUS_URL") ?: ""),
            "AGENT" to (System.getenv("AGENT") ?: "")
        )
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
    dependsOn(tasks.processResources)
}

// Shadow and relocation configuration
val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("")
    dependsOn(tasks.shadowJar)
    inputFile.set(tasks.shadowJar.get().archiveFile)
    destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
}

tasks.jar {
    archiveClassifier.set("without-deps")
    destinationDirectory.set(layout.buildDirectory.dir("intermediates"))
}

tasks.shadowJar {
    destinationDirectory.set(layout.buildDirectory.dir("intermediates"))
    archiveClassifier.set("non-obfuscated-with-deps")
    configurations = listOf(shadowModImpl, shadowImpl)

    doLast {
        configurations.forEach {
            println("Copying dependencies into mod: ${it.files}")
        }
    }
    exclude("META-INF/versions/**")
    relocate("io.github.notenoughupdates.moulconfig", "io.github.chindeaytb.collectiontracker.deps.moulconfig")
    relocate("io.github.chindeaytb.implementation", "io.github.chindeaytb.collectiontracker.deps.implementation")
}

tasks.assemble.get().dependsOn(tasks.remapJar)

tasks.jar {
    finalizedBy(cleanVersionConstants)
}

tasks.build {
    finalizedBy(cleanVersionConstants)
}

tasks.assemble {
    finalizedBy(cleanVersionConstants)
}