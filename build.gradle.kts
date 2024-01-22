// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed

buildscript {

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
        classpath (libs.kotlin.gradle.plugin)
        classpath (libs.gradle)
    }
    repositories {
        mavenCentral()
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id ("com.android.library") version "8.0.2" apply false
    id ("org.jetbrains.kotlin.jvm") version "1.6.10" apply false

}
true // Needed to make the Suppress annotation work for the plugins block