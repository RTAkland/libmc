plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.maven.publish)
}

allprojects {
    group = "cn.rtast.libmc"
    version = getProperty("libVersion")

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
        publications.withType<MavenPublication> {
            pom {
                name = "libmc"
                description = "Lightweight Minecraft client-side protocol library"
                url = "https://github.com/RTAkland/libmc"
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }
                developers {
                    developer {
                        id = "rtakland"
                        name = "RTAkland"
                    }
                }
                scm {
                    url = "https://github.com/RTAkland/libmc.git"
                }
            }
        }
    }
}

fun getProperty(name: String): String = providers.gradleProperty(name).get()