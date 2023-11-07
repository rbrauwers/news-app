plugins {
    id("com.rbrauwers.newapp.library.plugin")
    id("com.rbrauwers.newapp.library.compose.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
}

android {
    namespace = "com.rbrauwers.newsapp.source"
}

dependencies {
    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.constraintlayout.compose)

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
}

kapt {
    correctErrorTypes = true
}