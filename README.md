# mcping

A lightweight motd ping library for kotlin multiplatform and java,
no dependencies in `jvm` target except `kotlin-stdlib`

native platforms required `ktor-network` and `kotlinx-io`

# Supported targets

- jvm 1.8
- mingwX64
- linuxArm64
- liunxX64
- iosArm64
- iosSimulatorArm64
- macosArm64

# Get started

```kotlin
repositories {
    maven("https://repo.rtast.cn/packages")
}

dependencies {
    implementation("cn.rtast.mcping:mcping:0.0.1")
}
```

```kotlin
fun main() {
    // return a json respose
    val response: String = mcping("org.mc-complex.com", 25565)
    println(response)
}
```

> An example json response can be found at [ping-response-example](example/java-ping-response.json) (Formatted)

# Open Source

Open source under [Apache-2.0](LICENSE)