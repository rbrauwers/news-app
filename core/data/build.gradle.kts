plugins {
    id("com.rbrauwers.newapp.library.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
}

android {
    namespace = "com.rbrauwers.newsapp.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.paging.runtime)
}

kapt {
    correctErrorTypes = true
}