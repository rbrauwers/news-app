plugins {
    id("com.rbrauwers.newapp.application.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
    alias(libs.plugins.baseline.profiler)
}

android {
    namespace = "com.rbrauwers.newsapp"

    defaultConfig {
        applicationId = "com.rbrauwers.newsapp"
        versionCode = 1
        versionName = "1.0"
    }

    // [START macrobenchmark_setup_app_build_type]
    buildTypes {
        val release = getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // In real app, this would use its own release keystore
            signingConfig = signingConfigs.getByName("debug")
        }

        create("benchmark") {
            initWith(release)
            signingConfig = signingConfigs.getByName("debug")
            // [START_EXCLUDE silent]
            // Selects release buildType if the benchmark buildType not available in other modules.
            matchingFallbacks.add("release")
            // [END_EXCLUDE]
            proguardFiles("benchmark-rules.pro")
        }
    }
    // [END macrobenchmark_setup_app_build_type]

    /*
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    */

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    implementation(libs.coil.compose)
    implementation(libs.profile.installer)

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":features:headline"))
    implementation(project(":features:source"))

    kover(project(":core:data"))
    kover(project(":core:network"))
    kover(project(":core:model"))
    kover(project(":core:ui"))
    kover(project(":features:headline"))
    kover(project(":features:source"))

    baselineProfile(project(":benchmark"))
}

kapt {
    correctErrorTypes = true
}

koverReport {
    androidReports("debug") {

    }
}

baselineProfile {
    automaticGenerationDuringBuild = true
    //mergeIntoMain = true
}