plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
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
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.compose.bom))
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(projects.places)
            implementation(projects.signin)
        }
    }
}

android {
    namespace = "com.dilivva.googleex"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.dilivva.googleex"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>().configureEach {
//    if (outputKind != org.jetbrains.kotlin.konan.target.CompilerOutputKind.FRAMEWORK) return@configureEach
//    val iosResources = project.layout.buildDirectory.dir("kotlin-multiplatform-resources/aggregated-resources/iosSimulatorArm64/composeResources/googleexperimentals.places.generated.resources")
//    println("Ios: ${iosResources.get().asFile.absolutePath}")
//    println("Out: ${outputFile.get().absolutePath}")
//
//    doLast {
//        val out = providers.environmentVariable("BUILT_PRODUCTS_DIR")
//            .zip(
//                providers.environmentVariable("CONTENTS_FOLDER_PATH")
//            ) { builtProductsDir, contentsFolderPath ->
//                File("$builtProductsDir/$contentsFolderPath").canonicalPath
//            }
//            .flatMap {
//                project.objects.directoryProperty().apply { set(File(it)) }
//            }
//        println("Copying resources... ${out.get().asFile.absolutePath}")
//        iosResources.get().asFile.copyRecursively(out.get().asFile, true)
//    }
//}

