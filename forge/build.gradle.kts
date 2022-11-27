architectury {
    platformSetupLoomIde()
    forge()
}

val common by configurations.registering
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

    // Mod required mod dependencies
    implementation("thedarkcolour:kotlinforforge:${project.property("kotlin_forge_version").toString()}")
    modApi("me.shedaniel:architectury-forge:${project.property("architectury_version").toString()}")
    modApi("me.shedaniel.cloth:cloth-config-forge:${project.property("cloth_config_version").toString()}")

    // Optional mod dependencies
    modApi("vazkii.patchouli:Patchouli:${project.property("patchouli_version")}-SNAPSHOT:api")
    modApi("vazkii.patchouli:Patchouli:${project.property("patchouli_version")}-SNAPSHOT")

    // Dependencies for the mod
    bundle(
        "com.github.RPMTW:RPMTW-API-Client-Kotlin:${
            project.property("rpmtw_api_client_version").toString()
        }"
    ) {
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
    }.let {
        forgeRuntimeLibrary(it)
    }
    bundle(
        forgeRuntimeLibrary(
            group = "io.sentry",
            name = "sentry",
            version = project.property("sentry_version").toString()
        )
    )

    forgeRuntimeLibrary(kotlin("stdlib-jdk8", version = "1.6.10"))
    forgeRuntimeLibrary(kotlin("reflect", version = "1.6.10"))
    forgeRuntimeLibrary("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    "common"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    bundle(project(path = ":common", configuration = "transformProductionForge")) { isTransitive = false }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
    }
}

loom {
    accessWidenerPath.set(project(":common").file("src/main/resources/rpmtw_platform_mod.accesswidener"))

    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add("rpmtw_platform_mod.accesswidener")
        mixinConfig("rpmtw_platform_mod.mixins.json")
        mixinConfig("rpmtw_platform_mod.forge.mixins.json")
    }
}

repositories {
    maven {
        // Kotlin For Forge
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }

    maven {
        // Patchouli
        url = uri("https://maven.blamejared.com")
    }
    mavenCentral()
}