// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.3.10" apply false


}


buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath (libs.gradle)
        classpath(libs.gradle.v830)

    }
    dependencies {
        classpath(libs.google.services) // Latest version as of 2024
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.3.0") // Ensure latest Gradle version
        classpath("com.google.gms:google-services:4.3.15") // Firebase Plugin
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        }






}


