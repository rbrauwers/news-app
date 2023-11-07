import com.android.build.gradle.LibraryExtension
import com.rbrauwers.newsapp.buildlogic.convention.BuildLogicConstants
import com.rbrauwers.newsapp.buildlogic.convention.configureKotlinAndroid
import com.rbrauwers.newsapp.buildlogic.convention.configureTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            //val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            //val testLibs = extensions.getByType<VersionCatalogsExtension>().named("testLibs")

            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureTests(this)
                defaultConfig.targetSdk = BuildLogicConstants.targetSdkVersion
                defaultConfig.consumerProguardFiles("consumer-rules.pro")
            }

            /*
            configurations.configureEach {
                resolutionStrategy {
                    force(testLibs.findLibrary("junit").get())
                    // Temporary workaround for https://issuetracker.google.com/174733673
                    force("org.objenesis:objenesis:2.6")
                }
            }
             */

            /**
             * Only dependencies that is used by all library modules should be placed here.
             */
            dependencies {
            }
        }
    }
}