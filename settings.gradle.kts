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
}

rootProject.name = "UserMessages"
include(":app")
include(":benchmark")
include(":core")
include(":features:composer")
include(":features:usermessages")
include(":features:allmessages")
include(":common:domain")
include(":common:localstorage")
include(":common:network")
include(":common:ui")
