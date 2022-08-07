plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
    platformSetupLoomIde()
    fabric()
}

base.archivesName.set("${project.property("archives_base_name")}-fabric")

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
val shadowCommon by configurations.registering  // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
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

    modImplementation("vazkii.patchouli:Patchouli:${project.property("patchouli_version")}-FABRIC-SNAPSHOT")

    "common"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    "shadowCommon"(project(path = ":common", configuration = "transformProductionFabric")) { isTransitive = false }
    "shadowCommon"(
        "com.github.RPMTW:RPMTW-API-Client-Kotlin:${project.property("rpmtw_api_client_version")}"
    ) {
        exclude("com.google.code.gson")
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
    }.let {
        implementation(it)
    }


    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}")
}

val accessWidenerFile = project(":common").file("src/main/resources/rpmtw_platform_mod.accesswidener")

loom {
    accessWidenerPath.set(accessWidenerFile)
}

tasks {
    val resourcesPath = file("src/main/resources")
    // The access widener file is needed in :fabric project resources when the game is run.
    val copyAccessWidener by registering(Copy::class) {
        from(accessWidenerFile)
        into(resourcesPath)
    }

    processResources {
        dependsOn(copyAccessWidener)
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }


    shadowJar {
        configurations = listOf(shadowCommon.get())
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
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
        exclude("rpmtw_platform_mod.accesswidener")
        exclude("rpmtw_platform_mod.mixins.json")
    }
}

components.getByName<AdhocComponentWithVariants>("java") {
    withVariantsFromConfiguration(project.configurations.shadowRuntimeElements.get()) {
        skip()
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