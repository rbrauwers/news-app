plugins {
    id("com.rbrauwers.newapp.library.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.rbrauwers.newsapp.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.datastore)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.paging.runtime)

    testImplementation(project(":core:tests"))
}

kapt {
    correctErrorTypes = true
}