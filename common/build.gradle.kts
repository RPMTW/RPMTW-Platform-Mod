dependencies {
    // We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
    // Do NOT use other classes from fabric loader
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version").toString()}")
    // Remove the next line if you don't want to depend on the API
    modApi("dev.architectury:architectury:${project.property("architectury_version").toString()}")
    modApi("me.shedaniel.cloth:cloth-config:${project.property("cloth_config_version").toString()}")
    implementation(
        group = "com.github.RPMTW",
        name = "RPMTW-API-Client-Kotlin",
        version = project.property("rpmtw_api_client_version").toString()
    )
    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-gson:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:2.3.1")
    modApi(group = "io.sentry", name = "sentry", version = project.property("sentry_version").toString())
}

architectury {
    common(project.property("enabled_platforms").toString().split(","))
}

loom {
    accessWidenerPath.set(file("src/main/resources/rpmtw_platform_mod.accesswidener"))
}