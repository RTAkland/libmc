@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

kotlin {
    applyDefaultHierarchyTemplate {
        common {
            group("native") {
                withMingwX64()
                group("posix") {
                    withLinuxX64()
                    withLinuxArm64()
                    withApple()
                }
            }
        }
    }

    explicitApi()
    withSourcesJar()

    linuxX64()
    linuxArm64()
    macosArm64()
    mingwX64()
    jvm { compilerOptions.jvmTarget = JvmTarget.JVM_1_8 }

    sourceSets {
        commonMain.dependencies {
            api(project(":common"))
            api(project(":nbt"))
            implementation(project(":socket"))
        }

        jvmMain.dependencies {}

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.core)
        }

        jvmTest.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        nativeTest.dependencies {
            implementation(libs.ktor.client.curl)
        }
    }

    compilerOptions.freeCompilerArgs.addAll("-Xexpect-actual-classes")
}