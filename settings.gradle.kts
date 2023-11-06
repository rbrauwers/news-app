pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        // Default libs is generated automatically

        create("testLibs") {
            from(files("gradle/testLibs.versions.toml"))
        }
    }
}

rootProject.name = "News app"

include(":app")
include(":core:common")
include(":core:data")
include(":core:database")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":features:headline")
include(":features:source")
