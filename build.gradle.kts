plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.maven.publish)
}

allprojects {
    group = "cn.rtast.libmc"
    val libVersion = getProperty("libVersion")
    version = when (name) {
        "protocol" -> "${getProperty("protocolVersion")}-$libVersion"
        else -> libVersion
    }

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

fun getProperty(name: String): String = providers.gradleProperty(name).get()