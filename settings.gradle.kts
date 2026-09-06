plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "libmc"

includeSubModule(":common")
includeSubModule(":mcping")
includeSubModule(":rconlib")
includeSubModule(":protocol")
includeSubModule(":protocol-encrypt")
includeSubModule(":nbt")
includeSubModule(":snbt")

fun includeSubModule(name: String, path: String? = null) = include(name).also {
    project(name).projectDir = file(path ?: "libmc-${name.removePrefix(":")}")
}
