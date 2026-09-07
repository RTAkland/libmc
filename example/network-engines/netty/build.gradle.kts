import org.jetbrains.kotlin.gradle.dsl.JvmTarget

kotlin {
    explicitApi()
    withSourcesJar()

    jvm { compilerOptions.jvmTarget = JvmTarget.JVM_1_8 }

    sourceSets {
        jvmMain.dependencies {
            api(project(":protocol"))
            api(libs.netty.handler)
            api(libs.netty.buffer)
        }
    }
}