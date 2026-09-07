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
            implementation(libs.kotlinx.io)
            api(libs.kotlinx.coroutines)
        }

        jvmMain.dependencies {}

//        nativeMain.dependencies {
//            implementation(libs.ktor.network)
//        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        jvmTest.dependencies {

        }
    }

    compilerOptions.freeCompilerArgs.addAll("-Xexpect-actual-classes")
}