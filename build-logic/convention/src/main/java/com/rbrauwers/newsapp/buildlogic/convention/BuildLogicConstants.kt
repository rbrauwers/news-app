package com.rbrauwers.newsapp.buildlogic.convention

import org.gradle.api.JavaVersion

object BuildLogicConstants {
    const val minSdkVersion = 24
    const val compileSdkVersion = 34
    const val targetSdkVersion = 34
    const val buildTools = "34.0.0"
    val java = JavaVersion.VERSION_17
}