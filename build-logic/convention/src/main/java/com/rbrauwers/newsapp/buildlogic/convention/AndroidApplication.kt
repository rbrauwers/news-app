package com.rbrauwers.newsapp.buildlogic.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

/**
 * Configure Android application module.
 */
@Suppress("UnusedReceiverParameter")
internal fun Project.configureAndroidApplication(
    applicationExtension: ApplicationExtension,
) {
    applicationExtension.apply {
        buildToolsVersion = BuildLogicConstants.buildTools
        compileSdk = BuildLogicConstants.compileSdkVersion

        defaultConfig {
            targetSdk = BuildLogicConstants.targetSdkVersion
        }
    }
}