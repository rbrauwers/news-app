import com.android.build.api.dsl.ApplicationExtension
import com.rbrauwers.newsapp.buildlogic.convention.configureAndroidApplication
import com.rbrauwers.newsapp.buildlogic.convention.configureAndroidCompose
import com.rbrauwers.newsapp.buildlogic.convention.configureKotlinAndroid
import com.rbrauwers.newsapp.buildlogic.convention.configureTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Basic application module setup.
 */
@Suppress("unused")
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.kapt")
                apply("org.jetbrains.kotlinx.kover")
            }

            extensions.configure<ApplicationExtension> {
                configureAndroidApplication(this)
                configureAndroidCompose(this)
                configureKotlinAndroid(this)
                configureTests(this)
            }

            /**
             * Only dependencies that is used by all application modules should be placed here.
             */
            /*
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
            }
             */
        }
    }

}