@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.rbrauwers.newapp.test.plugin")
    alias(libs.plugins.baseline.profiler)
}

android {
    namespace = "com.rbrauwers.benchmark"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(testLibs.androidx.junit)
    implementation(testLibs.espresso.core)
    implementation(testLibs.uiautomator)
    implementation(testLibs.benchmark.macro.junit4)
}

/*
androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}
 */

baselineProfile {
    useConnectedDevices = true
}