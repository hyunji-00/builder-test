/*
 * Copyright (C) Inswave Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Inswave Systems <webmaster@inswave.com>, 2022.5
 */

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias (libs.plugins.android.application)
    alias (libs.plugins.ksp)
    alias (libs.plugins.kotlin.android)
}

/**
 * do not remove.
 */
apply(rootProject.file("app/matrix_mobile.gradle"))


// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(keystorePropertiesFile.inputStream())


val active = extra["active"] as String
val configApplicationName = extra["applicationName"] as String
val configApplicationId = extra["applicationId"] as String
val configVersionCode = (extra["versionCode"] as String).toInt()
val configVersionName = extra["versionName"] as String
val configMinSdk = (extra["minSdk"] as String).toInt()
val configMatrixMobileConfigJson = extra["matrixMobileConfigJson"] as String
val configPluginConfigJson = extra["pluginConfigJson"] as String
val configBuildConfigJson = extra["buildConfigJson"] as String
println("activeProfile = $active")

kotlin {
    jvmToolchain(11)
}

android {
    compileSdk = 35
    defaultConfig {
        applicationId = configApplicationId
        versionCode = configVersionCode
        versionName = configVersionName
        minSdk = configMinSdk
        targetSdk = 35
        multiDexEnabled = true

        vectorDrawables {
            useSupportLibrary = true
        }

        base.archivesBaseName = "${configApplicationName}_v${configVersionName}_${SimpleDateFormat("yyyyMMddHHmm").format(Date())}"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String?
            keyPassword = keystoreProperties["keyPassword"] as String?
            storeFile = file(keystoreProperties["storeFile"]!!)
            storePassword = keystoreProperties["storePassword"]?.toString()
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
        }
    }

    buildTypes.forEach { buildType ->
        buildType.apply {
            resValue ("string", "MatrixMobileConfig_AppName", configApplicationName)
            resValue ("string", "MatrixMobileConfig_Server", configMatrixMobileConfigJson.toByteArray().contentToString())
            resValue ("string", "MatrixMobileConfig_Active", active)
            resValue ("string", "MatrixMobileConfig_Plugin", configPluginConfigJson.toByteArray().contentToString())
            resValue ("string", "MatrixMobileConfig_BuildConfig", configBuildConfigJson.toByteArray().contentToString())
        }
    }

    /**
     * 빌드 시 assets 에 .DS_Store 제외 처리.
     */
    androidResources {
        val defaultIgnorePattern = ".DS_Store"
        ignoreAssetsPattern = defaultIgnorePattern
    }

    buildFeatures {
        viewBinding = true
    }
    namespace = configApplicationId
}

//noinspection UseTomlInstead
dependencies {
    
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.aar"))))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //START OF DEPENDENCIES (DO NOT REMOVE OR MODIFY THIS LINE.)
    implementation(libs.bundles.matrix.essential.kotlin)
    implementation(libs.bundles.matrix.essential.androidx)
    implementation(libs.bundles.matrix.essential.retrofit)
    implementation(libs.bundles.matrix.essential.etc)
    ksp(libs.bundles.matrix.essential.compiler)

    
    //BOTTOM OF DEPENDENCIES (DO NOT REMOVE OR MODIFY THIS LINE.)
}