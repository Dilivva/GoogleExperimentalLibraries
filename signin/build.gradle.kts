import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.konan.target.KonanTarget

/*
 * Copyright (C) 2024, Send24.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.publish)
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        val defFile = "src/nativeInterop/cinterop/SignIn.def"
        val target = when(it.konanTarget){
            KonanTarget.IOS_ARM64 -> "ios-arm64"
            else -> "ios-arm64_x86_64-simulator"
        }
        val googleAuthPath = "$projectDir/libs/GoogleSignIn/$target/Headers"
        val gtmSessionFetcher = "$projectDir/libs/GTMSessionFetcher/$target/Headers"


        it.compilations.getByName("main") {
            val GoogleSignIn by cinterops.creating {
                packageName("signin.GoogleSignIn")
                defFile(defFile)
                includeDirs(googleAuthPath, gtmSessionFetcher)
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines)
            implementation(compose.runtime)
            implementation(compose.ui)
        }
        androidMain.dependencies {
            implementation(libs.google.credentials)
            implementation(libs.google.credentials.play)
            implementation(libs.google.credentials.id)
        }
    }
}
android {
    namespace = "com.dilivva.signin"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

@Suppress("UnstableApiUsage")
mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01, true)
    val versionTxt = "1.0.0"
    val isDev = findProperty("env")?.equals("dev") ?: false
    val version = if (isDev) "1.0.0-SNAPSHOT" else versionTxt


    coordinates("com.dilivva", "google-signin", version)

    pom{
        name.set("GoogleSignIn")
        description.set("GoogleSignIn Experimental Kotlin multiplatform library")
        inceptionYear.set("2024")
        url.set("https://github.com/Dilivva/GoogleExperimentalLibraries")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/Dilivva/GoogleExperimentalLibraries/LICENSE")
                distribution.set("https://github.com/Dilivva/GoogleExperimentalLibraries/LICENSE")
            }
        }
        developers {
            developer {
                name.set("Ayodele Kehinde")
                url.set("https://github.com/ayodelekehinde")
                email.set("ayodelekehinde@send24.co")
                organization.set("Send24")
            }
        }
        scm {
            url.set("https://github.com/Dilivva/GoogleExperimentalLibraries/")
            connection.set("scm:git:git://github.com/Dilivva/GoogleExperimentalLibraries.git")
            developerConnection.set("scm:git:ssh://git@ggithub.com/Dilivva/GoogleExperimentalLibraries.git")
        }
    }

    signAllPublications()
}