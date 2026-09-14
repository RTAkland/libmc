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

    listOf(
        linuxX64(),
        linuxArm64(),
        mingwX64()
    ).forEach {
        it.compilations["main"].cinterops {
            create("aes") {
                definitionFile.set(file("src/cinterop/aes.def"))
                extraOpts("-libraryPath", file("src/cinterop/libs/${it.name}").absolutePath)
                includeDirs(layout.projectDirectory.dir("csrc/aes"))
            }
        }
    }
    macosArm64()
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
            implementation(libs.kotlinx.io)
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