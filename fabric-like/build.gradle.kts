architectury {
    common(project.property("enabled_platforms").toString().split(","))
}

base.archivesName.set("${project.property("archives_base_name")}-fabric-like")

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

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury-fabric:${project.property("architectury_version")}")
    modApi("me.shedaniel.cloth:cloth-config-fabric:${project.property("cloth_config_version")}") {
        exclude(module = "fabric-api")
    }

    modImplementation("vazkii.patchouli:Patchouli:${project.property("patchouli_version")}-FABRIC-SNAPSHOT")

    "compileClasspath"(project(path = ":common", configuration = "namedElements")) { isTransitive = false }

    modImplementation("com.terraformersmc:modmenu:4.0.0")
}

val accessWidenerFile = project(":common").file("src/main/resources/rpmtw_platform_mod.accesswidener")

loom {
    accessWidenerPath.set(accessWidenerFile)
}