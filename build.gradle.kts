plugins {
   // kotlin("jvm") version "2.0.0"
    kotlin("multiplatform") version "2.0.0"
}

group = "org.cocoawerks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //testImplementation(kotlin("test"))
}

//tasks.test {
//    useJUnitPlatform()
//}
kotlin {
    jvmToolchain(17)
    iosArm64()
    js(IR) {
        moduleName = "blackjack_sim"
        browser {
            webpackTask {
                mainOutputFileName = "blackjack_sim.js"
                output.libraryTarget = "umd"
            }
        }
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.freeCompilerArgs.add("-Xir-minimized-member-names=false")
            }
        }
        binaries.executable()
    }
}

