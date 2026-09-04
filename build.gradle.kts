plugins {
    kotlin("multiplatform") version "2.4.10" apply false
    id("maven-publish")
}

allprojects {
    group = "cn.rtast.libmc"
    version = providers.gradleProperty("libVersion").get()

    repositories {
        mavenCentral()
    }
}

subprojects {
    pluginManager.apply("org.jetbrains.kotlin.multiplatform")
    pluginManager.apply("maven-publish")

    publishing {
        repositories {
            maven("https://repo.rtast.cn/packages") {
                credentials(HttpHeaderCredentials::class) {
                    name = "Authorization"
                    value = "Bearer ${System.getenv("PUBLISH_TOKEN")}"
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }
}