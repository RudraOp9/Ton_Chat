import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.ComposeHotRun
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")



    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation("androidx.security:security-crypto:1.0.0")
            implementation(libs.bcpkix.jdk18on)


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

            implementation(compose.materialIconsExtended)

            // implementation("org.ton:ton-kotlin:0.2.18")
            implementation(libs.bundles.ktor)
            implementation("org.ton:ton-kotlin-crypto:0.4.3")
            implementation("com.russhwolf:multiplatform-settings:1.3.0")

            implementation(libs.navigation.compose)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation("org.ton:ton-kotlin-crypto:0.4.3")
            implementation("org.ton:ton-kotlin-contract:0.4.3")

            // implementation(libs.bundles.ktor)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        desktopMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}




compose.resources {
    publicResClass = true
    generateResClass = auto
}
/*configurations.all {
    resolutionStrategy.force("io.ktor:ktor-client-core:YOUR_CHOSEN_VERSION")
    resolutionStrategy.force("io.ktor:ktor-client-serialization:YOUR_CHOSEN_VERSION")
    // Add for all Ktor modules you use or ton-kotlin might use
}*/

android {
    namespace = "leo.decentralized.tonchat"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "leo.decentralized.tonchat"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        tasks.withType<ComposeHotRun>().configureEach {
            mainClass.set("leo.decentralized.tonchat.MainKt")
        }

        mainClass = "leo.decentralized.tonchat.MainKt"
        buildTypes.release.proguard {
            //runReleaseDistributable
            isEnabled = true  // false to disable proguard
            optimize = true
            obfuscate = true
            //  configurationFiles.from("proguard-android-optimize.pro")
        }
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "leo.decentralized.tonchat"
            packageVersion = "1.0.0"
            //description = "MyDescription"
            //copyright = "© 2022 MyApp. All rights reserved."
            //vendor = "MyCompany"
            windows {
                shortcut = true
                menuGroup = packageName
                //https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                //upgradeUuid = "28EA1N5A-D39A-4D09-A6FC-EFD835CFTG72"
            }

        }
    }
}
