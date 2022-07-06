plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    maven { url = uri("https://maven.quiltmc.org/repository/release/") }
    maven { url = uri("https://maven.quiltmc.org/repository/snapshot/") } // Kotlin currently is in snapshot

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

base.archivesName.set("${project.property("archives_base_name")}-quilt")

val common by configurations.registering
val shadowCommon by configurations.registering  // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
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
    modApi("org.quiltmc.quilted-fabric-api:quilted-fabric-api:${project.property("quilted_fabric_api_version")}-${project.property("minecraft_version")}")
    // Remove the next few lines if you don't want to depend on the API
    modApi("dev.architectury:architectury-fabric:${project.property("architectury_version")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.property("cloth_config_version")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi("org.quiltmc.quilt-kotlin-libraries:quilt-kotlin-libraries:${project.property("quilt_kotlin_libraries")}")

    modImplementation("vazkii.patchouli:Patchouli:${project.property("patchouli_version")}-FABRIC-SNAPSHOT") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }
    modImplementation("com.terraformersmc:modmenu:${project.property("modmenu_version")}") {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
    }

    "shadowCommon"(
        "com.github.RPMTW:RPMTW-API-Client-Kotlin:${project.property("rpmtw_api_client_version")}"
    ) {
        exclude("com.google.code.gson")
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.kotlin")
    }.let {
        implementation(it)
    }

    "common"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    "shadowCommon"(project(path = ":common", configuration = "transformProductionQuilt")) { isTransitive = false }
    "common"(project(path = ":fabric-like", configuration = "namedElements")) { isTransitive = false }
    "shadowCommon"(project(path = ":fabric-like", configuration = "transformProductionQuilt")) { isTransitive = false }
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
        inputs.property("group", project.group)
        inputs.property("version", project.version)

        filesMatching("quilt.mod.json") {
            expand("group" to project.group, "version" to project.version)
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
        create<MavenPublication>("mavenQuilt") {
            artifactId = project.property("archives_base_name").toString()
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {}
}