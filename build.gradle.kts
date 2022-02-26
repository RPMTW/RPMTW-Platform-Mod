import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    java
    id("org.jetbrains.kotlin.jvm") version "1.6.20-M1"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "0.10.0-SNAPSHOT" apply false
}

architectury {
    minecraft = project.property("minecraft_version").toString()
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    val loom = extensions.getByType(LoomGradleExtensionAPI::class)
    loom.silentMojangMappingsLicense()

    dependencies {
        "minecraft"("com.mojang:minecraft:${project.property("minecraft_version").toString()}")
        // The following line declares the mojmap mappings, you may use other mappings as well
        "mappings"(loom.officialMojangMappings())
        // The following line declares the yarn mappings you may select this one as well.
        // mappings ("net.fabricmc:yarn:${rootProject.yarn_version}")
        // 使用 yarn 會導致無法編譯 forge 版本，因此暫時先用 mojang 官方映射
        implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.6.20-M1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

    base.archivesName.set(project.property("archives_base_name").toString())
    version = project.property("mod_version").toString()
    group = project.property("maven_group").toString()

    repositories {
        // Add repositories to retrieve artifacts from in here.
        // You should only use this when depending on other mods because
        // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
        // See https://docs.gradle.org/current/userguide/declaring_repositories.html
        // for more information about repositories.
        maven {
            url = uri("https://jitpack.io")
        }
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(17)
        }

        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }

        "jar"(Jar::class) {
            from(rootProject.file("LICENSE"))
        }
    }

    java {
        withSourcesJar()
    }
}
