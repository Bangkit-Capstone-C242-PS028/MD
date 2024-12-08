// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // Tambahkan classpath untuk Hilt plugin di sini
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath ("com.google.dagger:hilt-compiler:2.48")

    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
}

