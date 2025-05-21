
import org.gradle.api.JavaVersion
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.cabaggregatorapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cabaggregatorapp"
        minSdk = 25
        //noinspection OldTargetApi,ExpiredTargetSdkVersion
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }



}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.appcompat)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.compose.v274)
    implementation(libs.material)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.runtime)
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.compose.foundation:foundation-layout:1.5.0")
    implementation("androidx.compose.material3:material3:1.2.0") // Use latest version
    implementation("androidx.compose.material:material:1.5.0")
    implementation("com.google.firebase:firebase-common:XX.X.X")
    implementation("com.google.firebase:firebase-auth:XX.X.X")
    implementation("com.google.firebase:firebase-firestore:XX.X.X")




    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation("com.google.android.gms:play-services-basement:18.2.0")





}
dependencies {
    implementation(libs.androidx.compose.material)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.auth)

}
var kotlinCompilerExtensionVersion = "1.5.3"
dependencies {
    implementation("com.google.firebase:firebase-auth-ktx:22.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.1") // If using Firestore
    implementation("com.google.android.gms:play-services-auth:20.7.0") // If using Google Sign-In
}

dependencies {
    implementation("com.google.firebase:firebase-common:20.4.1")  // Latest stable version
    implementation("com.google.firebase:firebase-firestore:24.8.1")
    implementation("com.google.firebase:firebase-auth:22.2.0")

    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-basement:18.2.0")
    implementation("com.google.firebase:firebase-bom:32.0.0")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.navigation:navigation-compose:2.7.5")



}
configurations.all {
    resolutionStrategy {
        force("com.google.firebase:firebase-common:20.4.1")
    }
}
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0") // Google Sign-In Dependency
}
dependencies {
    implementation("com.google.firebase:firebase-auth:21.1.0")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-core")

}
dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.compose.material:material-icons-extended:1.5.0")


    // Jetpack Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // Firebase (if required)
    implementation("com.google.firebase:firebase-bom:32.2.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // Compose UI
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.code.gson:gson:2.10.1")



}
apply(plugin = "com.google.gms.google-services")



