architectury {
    platformSetupLoomIde()
    fabric()
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://bai.jfrog.io/artifactory/maven") //ModMenu
        content {
            includeGroup("com.terraformersmc")
        }
    }
    maven {
        // Patchouli
        url = uri("https://maven.blamejared.com")
    }
}

val common by configurations.registering
configurations {
    compileClasspath {
        extendsFrom(common.get())
    }

    runtimeClasspath {
        extendsFrom(common.get())
    }

    getByName("developmentFabric").extendsFrom(common.get())
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury-fabric:${project.property("architectury_version")}")
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.property("cloth_config_version")}") {
        exclude(module = "fabric-api")
    }
    modApi("net.fabricmc:fabric-language-kotlin:${project.property("fabric-kotlin_version")}")

// Patchouli currently doesn't support Minecraft 1.20
//    modImplementation("vazkii.patchouli:Patchouli:${project.property("patchouli_version")}-FABRIC")

    "common"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    bundle(project(path = ":common", configuration = "transformProductionFabric")) { isTransitive = false }
    bundle(
        "com.github.RPMTW:RPMTW-API-Client-Kotlin:${project.property("rpmtw_api_client_version")}"
    ) {
        exclude("com.google.code.gson")
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
    }.let { implementation(it) }
    "common"(project(path = ":fabric-like", configuration = "namedElements")) { isTransitive = false }
    bundle(project(path = ":fabric-like", configuration = "transformProductionFabric")) { isTransitive = false }

    implementation(
        bundle(
            group = "io.sentry",
            name = "sentry",
            version = project.property("sentry_version").toString()
        )
    )

    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
}

val accessWidenerFile: File = project(":common").file("src/main/resources/rpmtw_platform_mod.accesswidener")

loom {
    accessWidenerPath.set(accessWidenerFile)
}

tasks {
    val generatedResourcesPath = file("src/generated/resources")
    // The access widener file is needed in :fabric project resources when the game is run.
    val copyAccessWidener by registering(Copy::class) {
        from(accessWidenerFile)
        into(generatedResourcesPath)
    }

    processResources {
        dependsOn(copyAccessWidener)
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
        exclude("rpmtw_platform_mod.accesswidener")
    }
}