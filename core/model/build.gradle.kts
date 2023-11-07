plugins {
    id("com.rbrauwers.newapp.library.plugin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.rbrauwers.newsapp.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}