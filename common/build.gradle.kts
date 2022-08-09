dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version").toString()}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury:${project.property("architectury_version").toString()}")
    modApi("me.shedaniel.cloth:cloth-config:${project.property("cloth_config_version").toString()}")
    modApi("com.github.RPMTW:RPMTW-API-Client-Kotlin:${project.property("rpmtw_api_client_version")}")
}

architectury {
    common(project.property("enabled_platforms").toString().split(","))
}

loom {
    accessWidenerPath.set(file("src/main/resources/rpmtw_platform_mod.accesswidener"))
}