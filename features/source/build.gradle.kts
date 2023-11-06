plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.rbrauwers.newsapp.source"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecyle.runtime)
    implementation(libs.activity.compose)
    implementation(libs.lifecyle.viewmodel)
    implementation(libs.lifecyle.viewmodel.compose)
    implementation(libs.lifecyle.compose)

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.material)
    implementation(libs.compose.icons.extended)
    implementation(libs.compose.tooling.preview)
    debugImplementation(libs.compose.tooling)
    implementation(libs.navigation.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.constraintlayout.compose)

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))

    testImplementation(testLibs.junit)
    androidTestImplementation(testLibs.androidx.junit)
    androidTestImplementation(testLibs.espresso.core)

    androidTestImplementation(testLibs.compose.junit4)
    debugImplementation(testLibs.compose.manifest)
}

kapt {
    correctErrorTypes = true
}