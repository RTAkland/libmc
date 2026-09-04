import org.jetbrains.kotlin.gradle.dsl.JvmTarget

kotlin {
    explicitApi()

    linuxX64()
    linuxArm64()
    macosArm64()
    mingwX64()
    iosArm64()
    iosSimulatorArm64()
    jvm { compilerOptions.jvmTarget = JvmTarget.JVM_1_8 }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":common"))
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}