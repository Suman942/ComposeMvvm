plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id ("kotlin-parcelize")

}

android {
    namespace = "com.example.composemvvm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.composemvvm"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


    configurations {
        all {
            // Exclude the conflicting annotations
            exclude(group = "com.intellij", module = "annotations")
        }
    }
}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}


dependencies {
    implementation(libs.core.ktx) {
        // Exclude the conflicting annotations transitively brought in
        exclude(group = "com.intellij", module = "annotations")
    }

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil)

    val hiltVersion = "2.50"
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    val hiltNavigationCompose = "1.0.0"
    implementation("androidx.hilt:hilt-navigation-compose:$hiltNavigationCompose")
    implementation("androidx.hilt:hilt-navigation-fragment:$hiltNavigationCompose")
    implementation("androidx.hilt:hilt-compiler:$hiltNavigationCompose")
    annotationProcessor("androidx.hilt:hilt-compiler:$hiltNavigationCompose")

    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    val lifecycle = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")

    implementation("androidx.compose.runtime:runtime-livedata")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    val nav_version = "2.7.6"
    implementation("androidx.navigation:navigation-compose:$nav_version")
}