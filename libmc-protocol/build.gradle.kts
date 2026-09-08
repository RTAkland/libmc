import org.jetbrains.kotlin.gradle.dsl.JvmTarget

kotlin {
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
        }

        jvmMain.dependencies {}

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.network)
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