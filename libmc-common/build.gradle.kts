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
        jvmMain.dependencies {
            // no dependencies needed
        }

        nativeMain.dependencies {
            implementation("io.ktor:ktor-network:3.5.2")
            implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.9.1")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.11.0")
        }
    }

    compilerOptions.freeCompilerArgs.addAll("-Xexpect-actual-classes")
}