import com.android.build.gradle.TestExtension
import com.rbrauwers.newsapp.buildlogic.convention.BuildLogicConstants
import com.rbrauwers.newsapp.buildlogic.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Should be used only for modules that uses com.android.test plugin (e.g. benchmark modules).
 */
@Suppress("unused")
class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            //val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            //val testLibs = extensions.getByType<VersionCatalogsExtension>().named("testLibs")

            with(pluginManager) {
                apply("com.android.test")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
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