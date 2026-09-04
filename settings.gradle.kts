plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "libmc"

includeSubModule(":common")
includeSubModule(":mcping")
includeSubModule(":rconlib")
includeSubModule(":chat")
//includeSubModule(":nbt")

fun includeSubModule(name: String) = include(name).also {
    project(name).projectDir = file("libmc-${name.removePrefix(":")}")
}
