plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "libmc"

includeSubModule("common")
includeSubModule("protocol")
includeSubModule("protocol-context")
includeSubModule("nbt")
//includeSubModule("snbt")

//includeSubModule("protocol-engine-netty", path = "libmc-network-engines/netty")
//includeSubModule("protocol-engine-ktor-network", path = "libmc-network-engines/ktor-network")

fun includeSubModule(name: String, path: String? = null) {
    include(":$name")
    project(":$name").projectDir = file(path ?: "libmc-$name")
}