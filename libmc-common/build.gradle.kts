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
        commonMain.dependencies {}
        jvmMain.dependencies {}

        nativeMain.dependencies {
            implementation(libs.kotlinx.io)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }

    compilerOptions.freeCompilerArgs.addAll("-Xexpect-actual-classes")
}