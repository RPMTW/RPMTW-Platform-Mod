plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    maven { url = uri("https://maven.quiltmc.org/repository/release/") }

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

architectury {
    platformSetupLoomIde()
    loader("quilt")
}

val common by configurations.registering
configurations {
    compileClasspath {
        extendsFrom(common.get())
    }

    runtimeClasspath {
        extendsFrom(common.get())
    }

    getByName("developmentQuilt").extendsFrom(common.get())
}

val accessWidenerFile = project(":common").file("src/main/resources/rpmtw_platform_mod.accesswidener")

loom {
    accessWidenerPath.set(accessWidenerFile)
}

dependencies {
    modImplementation("org.quiltmc:quilt-loader:${project.property("quilt_loader_version")}")
    modImplementation("org.quiltmc:qsl:${project.property("qsl_version")}+${project.property("minecraft_version")}")
    modApi(
        "org.quiltmc.quilted-fabric-api:quilted-fabric-api:${project.property("quilted_fabric_api_version")}-${
            project.property(
                "minecraft_version"
            )
        }"
    )

    // Change the kotlin adapter back to Fabric Language Kotlin due to the expected breaking changes on QKL
    modApi("net.fabricmc:fabric-language-kotlin:${project.property("fabric-kotlin_version")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }

    // Remove the next few lines if you don't want to depend on the API
    modApi("dev.architectury:architectury-fabric:${project.property("architectury_version")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.property("cloth_config_version")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }

    modImplementation("vazkii.patchouli:Patchouli:${project.property("patchouli_version")}-FABRIC") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }
    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }

    bundle(
        "com.github.RPMTW:RPMTW-API-Client-Kotlin:${project.property("rpmtw_api_client_version")}"
    ) {
        exclude("com.google.code.gson")
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
    }.let {
        implementation(it)
    }

    "common"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    bundle(project(path = ":common", configuration = "transformProductionQuilt")) { isTransitive = false }
    "common"(project(path = ":fabric-like", configuration = "namedElements")) { isTransitive = false }
    bundle(project(path = ":fabric-like", configuration = "transformProductionQuilt")) { isTransitive = false }

    implementation(
        bundle(
            group = "io.sentry",
            name = "sentry",
            version = project.property("sentry_version").toString()
        )
    )
}

tasks {
    val resourcesPath = file("src/main/resources")
    // The access widener file is needed in :quilt project resources when the game is run.
    val copyAccessWidener by registering(Copy::class) {
        from(accessWidenerFile)
        into(resourcesPath)
    }

    processResources {
        dependsOn(copyAccessWidener)
        inputs.property("group", project.group)
        inputs.property("version", project.version)

        filesMatching("quilt.mod.json") {
            expand("group" to project.group, "version" to project.version)
        }
    }

    sourcesJar {
        val commonSources = project(":common").tasks.sourcesJar
        dependsOn(commonSources)
        from(commonSources.get().archiveFile.map { zipTree(it) })
        exclude("rpmtw_platform_mod.accesswidener")
        exclude("rpmtw_platform_mod.mixins.json")
    }
}