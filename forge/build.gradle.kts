plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
    platformSetupLoomIde()
    forge()
}

val common by configurations.registering
val shadowCommon by configurations.registering  // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
configurations {
    compileClasspath {
        extendsFrom(common.get())
    }

    runtimeClasspath {
        extendsFrom(common.get())
    }

    getByName("developmentForge").extendsFrom(common.get())
}

dependencies {
    forge("net.minecraftforge:forge:${project.property("forge_version").toString()}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury-forge:${project.property("architectury_version").toString()}")
    modApi("me.shedaniel.cloth:cloth-config-forge:${project.property("cloth_config_version").toString()}")
    implementation("thedarkcolour:kotlinforforge:${project.property("kotlin_forge_version").toString()}")

    "shadowCommon"(
        "com.github.RPMTW:RPMTW-API-Client-Kotlin:${
            project.property("rpmtw_api_client_version").toString()
        }"
    ) {
        exclude("com.google.code.gson")
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
    }.let {
        forgeRuntimeLibrary(it)
    }

    forgeRuntimeLibrary("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10")
    forgeRuntimeLibrary("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    "common"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    "shadowCommon"(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        exclude("fabric.mod.json")

        configurations = listOf(shadowCommon.get())
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        input.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        archiveClassifier.set(null as String?)
    }

    jar {
        archiveClassifier.set("dev")
    }

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
    }
}

components.getByName<AdhocComponentWithVariants>("java") {
    withVariantsFromConfiguration(project.configurations.shadowRuntimeElements.get()) {
        skip()
    }
}

loom {
    forge {
        mixinConfig("rpmtw_platform_mod.mixins.json")
    }
}

repositories {
    maven {
        // Kotlin For Forge
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenCommon") {
            artifactId = project.property("archives_base_name").toString()
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {}
}