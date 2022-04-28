rootProject.buildFileName = "build.gradle.kts"

// Set single lock file (gradle.lockfile)
// This preview feature should be enabled by default in Gradle 7
// More: https://docs.gradle.org/current/userguide/dependency_locking.html#single_lock_file_per_project
enableFeaturePreview("ONE_LOCKFILE_PER_PROJECT")
enableFeaturePreview("VERSION_CATALOGS")

enableFeaturePreview("VERSION_CATALOGS")

include(
    ":app",
    ":core",
)

// Gradle plugins are added via plugin management, not the classpath
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }

    // Using the plugins  DSL allows generating type-safe accessors for Kotlin DSL
    plugins {
        // See Dependency management section in README.md
        // https://github.com/igorwojda/android-showcase#dependency-management
        val agpVersion: String by settings
        id("com.android.application") version agpVersion
        id("com.android.library") version "7.1.1"
        // id("com.android.dynamic-feature") version agpVersion

        val kotlinVersion: String by settings
        id("org.jetbrains.kotlin.jvm") version kotlinVersion
        id("org.jetbrains.kotlin.android") version "1.5.30"

        val navigationVersion: String by settings
        id("androidx.navigation.safeargs.kotlin") version navigationVersion

        val detektVersion: String by settings
        id("io.gitlab.arturbosch.detekt") version detektVersion

        val ktlintGradleVersion: String by settings
        id("org.jlleitschuh.gradle.ktlint") version ktlintGradleVersion

        val androidJUnit5Version: String by settings
        id("de.mannodermaus.android-junit5") version androidJUnit5Version
    }

    resolutionStrategy {
        eachPlugin {

            when (requested.id.id) {
                "com.android.application",
                "com.android.library" -> {
                    val agpCoordinates: String by settings
                    useModule(agpCoordinates)
                }
//                "com.android.dynamic-feature" -> {
//                    val agpCoordinates: String by settings
//                    useModule(agpCoordinates)
//                }
                "androidx.navigation.safeargs.kotlin" -> {
                    val navigationCoordinates: String by settings
                    useModule(navigationCoordinates)
                }
                "de.mannodermaus.android-junit5" -> {
                    val androidJnit5Coordinates: String by settings
                    useModule(androidJnit5Coordinates) // navigationVersion
                }
            }
        }
    }
}

// See Dependency management section in README.md
// https://github.com/igorwojda/android-showcase#dependency-management
@Suppress("detekt.StringLiteralDuplication")
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {

            val kotlinVersion: String by settings
            version("kotlin", kotlinVersion)
            // Required by Android dynamic feature modules and SafeArgs
            alias("kotlin-reflect").to("org.jetbrains.kotlin", "kotlin-reflect").versionRef("kotlin")
            version("coroutines", "1.+")
            alias("coroutines").to("org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef("coroutines")
            bundle("kotlin", listOf("kotlin-reflect", "coroutines"))

            version("retrofit", "2.+")
            alias("retrofit-core").to("com.squareup.retrofit2", "retrofit").versionRef("retrofit")
            alias("converter-gson").to("com.squareup.retrofit2", "converter-gson").versionRef("retrofit")
            bundle("retrofit", listOf("retrofit-core", "converter-gson"))

            // Retrofit will use okhttp 4 (it has binary capability with okhttp 3)
            // See: https://square.github.io/okhttp/upgrading_to_okhttp_4/
            version("okhttp", "4.+")
            alias("okhttp-okhttp").to("com.squareup.okhttp3", "okhttp").versionRef("okhttp")
            alias("okhttp-interceptor").to("com.squareup.okhttp3", "logging-interceptor").versionRef("okhttp")
            // bundle is basically an alias for several dependencies
            bundle("okhttp", listOf("okhttp-okhttp", "okhttp-interceptor"))

            version("stetho", "1.5.0") // 1.5.1 has critical bug and newer version is unlikely to be release
            alias("stetho-core").to("com.facebook.stetho", "stetho").versionRef("stetho")
            alias("stetho-okhttp3").to("com.facebook.stetho", "stetho-okhttp3").versionRef("stetho")
            bundle("stetho", listOf("stetho-core", "stetho-okhttp3"))

            version("kodein", "6.+")
            // version("kodeincompose", "7.+")
            // Required by Android dynamic feature modules and SafeArgs
            alias("kodein-core").to("org.kodein.di", "kodein-di-generic-jvm").versionRef("kodein")
            alias("kodein-android-x").to("org.kodein.di", "kodein-di-framework-android-x").versionRef("kodein")
           //  alias("kodein-compose").to("org.kodein.di", "kodein-di-framework-compose").versionRef("kodeincompose")
            bundle("kodein", listOf("kodein-core", "kodein-android-x"))

            alias("timber").to("com.jakewharton.timber:timber:4.+")
            alias("constraintlayout").to("androidx.constraintlayout:constraintlayout:2.+")
            alias("coordinatorlayout").to("androidx.coordinatorlayout:coordinatorlayout:1.+")
            alias("appcompat").to("androidx.appcompat:appcompat:1.+")
            alias("recyclerview").to("androidx.recyclerview:recyclerview:1.+")
            alias("material").to("com.google.android.material:material:1.+")
            alias("play-core").to("com.google.android.play:core:1.+")

            alias("core-ktx").to("androidx.core:core-ktx:1.+")
            alias("fragment-ktx").to("androidx.fragment:fragment-ktx:1.+")
            bundle("ktx", listOf("core-ktx", "fragment-ktx"))

            version("lifecycle", "2.+")
            alias("viewmodel-ktx").to("androidx.lifecycle", "lifecycle-viewmodel-ktx").versionRef("lifecycle")
            alias("livedata-ktx").to("androidx.lifecycle", "lifecycle-livedata-ktx").versionRef("lifecycle")
            alias("lifecycle-common").to("androidx.lifecycle", "lifecycle-common-java8").versionRef("lifecycle")
            bundle("lifecycle", listOf("viewmodel-ktx", "livedata-ktx", "lifecycle-common"))

            val navigationVersion: String by settings
            version("navigation", navigationVersion)
            alias("navigation-fragment").to("androidx.navigation", "navigation-fragment-ktx").versionRef("navigation")
//            alias("navigation-dynamic")
//                .to("androidx.navigation", "navigation-dynamic-features-fragment")
//                .versionRef("navigation")
            alias("navigation-ui-ktx").to("androidx.navigation", "navigation-ui-ktx").versionRef("navigation")
            // bundle("navigation", listOf("navigation-fragment", "navigation-dynamic", "navigation-ui-ktx"))
            bundle("navigation", listOf("navigation-fragment", "navigation-ui-ktx"))

            alias("qrcode").to("com.budiyev.android:code-scanner:2.1.0")
            alias("otp").to("com.github.aabhasr1:OtpView:v1.1.2-ktx")
            alias("shimmer").to("com.facebook.shimmer:shimmer:0.5.0")
            alias("dialog").to("io.github.vanpra.compose-material-dialogs:datetime:0.6.1")


            alias("composeActivity").to("androidx.activity:activity-compose:1.3.1")
            alias("composeMaterial").to("androidx.compose.material:material:1.0.1")
            alias("composeAnimation").to("androidx.compose.animation:animation:1.0.1")
            alias("composeTooling").to("androidx.compose.ui:ui-tooling:1.0.1")
            alias("composeUITests").to("androidx.compose.ui:ui-test-junit4:1.0.1")


            alias("composeViewModel").to("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")

            alias("composeMaterialTheme").to("com.google.android.material:compose-theme-adapter:1.0.1")
            alias("composeRuntime").to("androidx.compose.runtime:runtime:1.0.1")
            alias("composeLiveData").to("androidx.compose.runtime:runtime-livedata:1.0.1")
            alias("composeExtendedIcons").to("androidx.compose.material:material-icons-extended:1.0.1")

            // AWS Dependencies
            alias("awsAndroidSdkCore").to("com.amazonaws:aws-android-sdk-core:2.35.0")
            alias("awsAndroidSdkCognitoIdentityProvider").to("com.amazonaws:aws-android-sdk-cognitoidentityprovider:2.35.0")

            // QrCode Generation
            alias("zxingCore").to("com.google.zxing:core:3.4.0")
            alias("zxingAndroid").to("com.journeyapps:zxing-android-embedded:3.6.0")

            // Preferences DataStore
            alias("dataStorePreferences").to("androidx.datastore:datastore-preferences:1.0.0")

            // Test dependencies
            alias("test-coroutines").to("org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef("coroutines")

            version("kluent", "1.+")
            alias("kluent-core").to("org.amshove.kluent", "kluent").versionRef("kluent")
            alias("kluent-android").to("org.amshove.kluent", "kluent-android").versionRef("kluent")

            alias("rhinoScriptEngine").to("io.apisense:rhino-android:1.1.1")

            alias("test-runner").to("androidx.test:runner:1.+")
            alias("espresso").to("androidx.test.espresso:espresso-core:3.+")
            alias("mockk").to("io.mockk:mockk:1.+")
            alias("arch").to("androidx.arch.core:core-testing:2.+")

            version("acompanist-version", "0.19.0")
            alias("pager").to("com.google.accompanist", "accompanist-pager").versionRef("acompanist-version")
            alias("insets").to("com.google.accompanist", "accompanist-insets").versionRef("acompanist-version")
            alias("indicators").to("com.google.accompanist", "accompanist-pager-indicators").versionRef("acompanist-version")

            alias("exoPlayer").to("com.google.android.exoplayer:exoplayer:2.13.3")
            alias("exoPlayerFullScreen").to("com.github.norulab:android-exoplayer-fullscreen:1.2.1")

            alias("eventBus").to("org.greenrobot:eventbus:3.2.0")
            alias("customActivityOnCrash").to("cat.ereza:customactivityoncrash:2.3.0")

            version("junit", "5.+")
            alias("junit-jupiter-api").to("org.junit.jupiter", "junit-jupiter-api").versionRef("junit")

            bundle(
                "test",
                listOf(
                    "test-coroutines",
                    "kluent-android",
                    "test-runner",
                    "espresso",
                    "mockk",
                    "arch",
                    "junit-jupiter-api"
                )
            )

            alias("junit-jupiter-engine").to("org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
        }
    }
}
