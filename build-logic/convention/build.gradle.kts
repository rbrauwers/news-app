plugins {
    `kotlin-dsl`
}

group = "com.rbrauwers.newsapp.build.logic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.build.tools)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.rbrauwers.newapp.application.plugin"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.rbrauwers.newapp.library.plugin"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "com.rbrauwers.newapp.library.compose.plugin"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("hilt") {
            id = "com.rbrauwers.newapp.hilt.plugin"
            implementationClass = "HiltConventionPlugin"
        }
    }
}