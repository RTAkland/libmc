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
            implementation(libs.cryptography.core)
            implementation(libs.cryptography.provider.optimal)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.network)
        }

        jvmTest.dependencies {
            implementation(libs.ktor.client.okhttp)
        }

        linuxTest.dependencies {
            implementation(libs.ktor.client.curl)
        }

        mingwTest.dependencies {
            implementation(libs.ktor.client.winhttp)
        }

        appleTest.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(project(":protocol"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}