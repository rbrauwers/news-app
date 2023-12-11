plugins {
    id("com.rbrauwers.newapp.application.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
}

android {
    namespace = "com.rbrauwers.newsapp"

    defaultConfig {
        applicationId = "com.rbrauwers.newsapp"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

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
}

kapt {
    correctErrorTypes = true
}

koverReport {
    androidReports("debug") {

    }
}