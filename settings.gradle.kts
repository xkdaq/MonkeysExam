pluginManagement {
    repositories {
        maven {
            setUrl("https://jitpack.io")
        }
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven {
            setUrl("https://jitpack.io")
        }
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
    }
}

rootProject.name = "MonkeysExam"
include(":app")
 