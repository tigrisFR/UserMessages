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
/*
    versionCatalogs {
        create("libs") {
            version("lifecycleVersion", "8.37")
            library("lifecycle-viewmodel-ktx",      "androidx.lifecycle","lifecycle-viewmodel-ktx").versionRef("lifecycleVersion")
            library("lifecycle-viewmodel-compose",  "androidx.lifecycle","lifecycle-viewmodel-compose").versionRef("lifecycleVersion")
            library("lifecycle-runtime-ktx",        "androidx.lifecycle","lifecycle-runtime-ktx").versionRef("lifecycleVersion")
            library("lifecycle-runtime-compose",    "androidx.lifecycle","lifecycle-runtime-compose").versionRef("lifecycleVersion")

            version("navigation","2.7.6")
            library("navigation-runtime-ktx",   "androidx.navigation","navigation-runtime-ktx").versionRef("navigation")
            library("navigation-compose",       "androidx.navigation","navigation-compose").versionRef("navigation")

            bundle("composeFeatureDependencies",
                listOf(
                    "lifecycle-viewmodel-ktx",
                    "lifecycle-viewmodel-compose",
                    "lifecycle-runtime-ktx",
                    "lifecycle-runtime-compose",

                    "navigation-runtime-ktx",
                    "navigation-compose",
                )
            )
        }
    }
 */
}

rootProject.name = "UserMessages"
include(":app")
include(":benchmark")
include(":core")
include(":features:composer")
