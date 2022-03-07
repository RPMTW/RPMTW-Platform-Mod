dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version").toString()}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury:${project.property("architectury_version").toString()}")
    modApi("me.shedaniel.cloth:cloth-config:${project.property("cloth_config_version").toString()}")
    modApi("com.github.RPMTW:RPMTW-API-Client-Kotlin:${project.property("rpmtw_api_client_version").toString()}")
}

architectury {
    common()
}

loom {
    accessWidenerPath.set(file("src/main/resources/rpmtw_platform_mod.accesswidener"))
}

publishing {
    publications {
        create<MavenPublication>("mavenCommon") {
            artifactId = project.property("archives_base_name").toString()
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
    }
}