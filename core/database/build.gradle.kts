plugins {
    id("com.rbrauwers.newapp.library.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
}

android {
    namespace = "com.rbrauwers.newsapp.database"
}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)

    implementation(project(":core:model"))
}