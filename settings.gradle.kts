pluginManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        flatDir { dirs("libs") }
        mavenLocal()
        gradlePluginPortal()
    }
}

include(
    ":app",
    ":matrix-mobile-plugins"
)

project(":matrix-mobile-plugins").projectDir = File("plugins")