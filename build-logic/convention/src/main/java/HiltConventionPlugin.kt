import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("dagger.hilt.android.plugin")
            pluginManager.apply("org.jetbrains.kotlin.kapt")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", libs.findLibrary("hilt-android").get())
                add("kapt", libs.findLibrary("hilt-compiler").get())
                add("kapt", libs.findLibrary("hilt-androidx-compiler").get())

                add("androidTestImplementation", libs.findLibrary("hilt-android-testing").get())
                add("kaptAndroidTest", libs.findLibrary("hilt-compiler").get())

                add("testImplementation", libs.findLibrary("hilt-android-testing").get())
                add("kaptTest", libs.findLibrary("hilt-compiler").get())
            }
        }
    }

}