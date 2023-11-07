package com.rbrauwers.newsapp.buildlogic.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Configure test-specific options
 */
internal fun Project.configureTests(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        /**
         * Only test dependencies that is used by all modules should be placed here.
         */
        val libs = extensions.getByType<VersionCatalogsExtension>().named("testLibs")
        dependencies {
            add("testImplementation", libs.findLibrary("junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
            add("androidTestImplementation", libs.findLibrary("espresso-core").get())
        }
    }
}