plugins {
    id("com.rbrauwers.newapp.library.plugin")
    id("com.rbrauwers.newapp.library.compose.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
}

android {
    namespace = "com.rbrauwers.newsapp.headline"
}

dependencies {
    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.constraintlayout.compose)

    implementation(libs.paging.compose)
    implementation(libs.paging.runtime)

    implementation(project(":core:common"))
    implementation(project(":core:data"))
    // TODO: check if we can figure out other solution (required due Pager)
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
}

kapt {
    correctErrorTypes = true
}