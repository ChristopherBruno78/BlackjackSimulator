plugins {
    `java-library`
    kotlin("multiplatform") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "org.cocoawerks"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

kotlin {
    jvm()
    macosArm64() {
        binaries {
            executable {
                baseName = "BlackjackSim"
            }
        }
    }
    iosSimulatorArm64() {
        binaries {
            framework {
                baseName = "BlackjackSim"
            }
        }
    }
    iosArm64() {
        binaries {
            framework {
                baseName = "BlackjackSim"
            }
        }
    }
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

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
            }
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
    jvmToolchain(17)
}

