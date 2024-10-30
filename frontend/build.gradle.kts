// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Default
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

    // Additional
    alias(libs.plugins.kotlin.ksp) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
}

buildscript {
    dependencies {
        classpath(libs.dagger.hilt.android.gradle.plugin)
    }
}