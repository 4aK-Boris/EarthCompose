import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "ru.mpei"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(dependencyNotation = "io.insert-koin:koin-core:3.3.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(dependencyNotation = "io.insert-koin:koin-test:3.3.0")
                implementation(dependencyNotation = "io.insert-koin:koin-test-junit4:3.3.0")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "ru.mpei.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "EarthCompose"
            packageVersion = "1.0.0"
            windows {
                iconFile.set(project.file("title.ico"))
            }
        }
    }
}
