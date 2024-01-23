plugins {
    id("com.rbrauwers.newapp.library.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
    alias(libs.plugins.apollo)
    alias(libs.plugins.room)
}

android {
    namespace = "com.rbrauwers.newsapp.database"
}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room)
    implementation(libs.room.paging)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)

    implementation(libs.paging.runtime)

    implementation(project(":core:model"))
}

room {
    schemaDirectory("$projectDir/schemas")
}