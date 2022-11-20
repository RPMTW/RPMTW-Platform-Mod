pluginManagement {
    repositories {
        maven { url = uri("https://maven.fabricmc.net/") }
        maven { url = uri("https://maven.architectury.dev/") }
        maven { url = uri("https://maven.minecraftforge.net/") }
        gradlePluginPortal()
    }
}

include("common")
include("fabric-like")
include("fabric")
include("quilt")
include("forge")

rootProject.name = "rpmtw-platform-mod"