// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:1.3.0")
    }
}

plugins {
    // Existing plugin aliases (fine!)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
