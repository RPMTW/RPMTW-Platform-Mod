import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("architectury-plugin") version "3.4.143"
    id("dev.architectury.loom") version "0.12.0.301" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

architectury {
    minecraft = project.property("minecraft_version").toString()
}

subprojects {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    val loom = extensions.getByType(LoomGradleExtensionAPI::class)
    loom.silentMojangMappingsLicense()

    repositories {
        mavenCentral()
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:${project.property("minecraft_version").toString()}")
        // The following line declares the mojmap mappings, you may use other mappings as well
        "mappings"(loom.officialMojangMappings())
        // The following line declares the yarn mappings you may select this one as well.
        // mappings ("net.fabricmc:yarn:${rootProject.yarn_version}")
        // 使用 yarn 會導致無法編譯 forge 版本，因此暫時先用 mojang 官方映射
        implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = "1.6.10")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    }

    // Set up platform subprojects (non-common subprojects).
    if (path != ":common") {
        apply(plugin = "com.github.johnrengelman.shadow")
        base.archivesName.set(project.property("archives_base_name").toString())

        // Define the "bundle" configuration which will be included in the shadow jar.
        val bundle by configurations.creating {
            isCanBeConsumed = false
            isCanBeResolved = true
        }

        tasks {
            "jar"(Jar::class) {
                archiveClassifier.set("dev")
            }

            "shadowJar"(ShadowJar::class) {
                archiveClassifier.set("dev-shadow")
                // Include our bundle configuration in the shadow jar.
                configurations = listOf(bundle)

                relocate("okhttp3", "com.rpmtw.rpmtw_platform_mod.shadow.okhttp3")
                relocate("okio", "com.rpmtw.rpmtw_platform_mod.shadow.okio")
                relocate("org.json", "com.rpmtw.rpmtw_platform_mod.shadow.org.json")
                relocate("io.sentry", "com.rpmtw.rpmtw_platform_mod.shadow.io.sentry")
                relocate("io.socket", "com.rpmtw.rpmtw_platform_mod.shadow.io.socket")
                relocate("com.github.kittinunf", "com.rpmtw.rpmtw_platform_mod.shadow.com.github.kittinunf")
            }

            "remapJar"(RemapJarTask::class) {
                dependsOn("shadowJar")
                // Replace the remap jar task's input with the shadow jar containing the common classes.
                inputFile.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
                // The project name will be "fabric" or "forge", so this will become the classifier/suffix
                // for the jar. For example: rpmtw-platform-mod-1.19-<version>-<platform>.jar
                archiveClassifier.set(project.name)
            }
        }
    }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "architectury-plugin")
    apply(plugin = "maven-publish")

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