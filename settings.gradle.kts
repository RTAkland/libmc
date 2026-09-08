pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.rtast.cn/packages/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "libmc"

includeSubModule("common")
includeSubModule("protocol")
includeSubModule("nbt")

fun includeSubModule(name: String, path: String? = null) {
    include(":$name")
    project(":$name").projectDir = file(path ?: "libmc-$name")
}