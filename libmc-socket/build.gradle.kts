@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

kotlin {
    explicitApi()
    withSourcesJar()

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

    linuxX64()
    linuxArm64()
    macosArm64()
    mingwX64()
    jvm { compilerOptions.jvmTarget = JvmTarget.JVM_1_8 }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }

    compilerOptions.freeCompilerArgs.addAll("-Xexpect-actual-classes")
}