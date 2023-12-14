import java.io.FileInputStream
import java.util.*

plugins {
    id("com.rbrauwers.newapp.library.plugin")
    id("com.rbrauwers.newapp.hilt.plugin")
    alias(libs.plugins.apollo)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.rbrauwers.newsapp.network"

    val properties = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "local.properties")))
    }

    defaultConfig {
        buildConfigField("String", "NEWS_API_KEY", properties.getProperty("newsApiKey"))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.apollo.runtime)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.serialization.converter)

    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    implementation(project(":core:model"))
}

kapt {
    correctErrorTypes = true
}

apollo {
    service("service") {
        packageName.set("com.rbrauwers.newsapp.network")
        nullableFieldStyle.set("none")
    }
}