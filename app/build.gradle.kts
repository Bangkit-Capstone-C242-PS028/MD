plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt) // Alias untuk Hilt
    kotlin("kapt") // Alias untuk Kapt
    id("com.google.gms.google-services")
//    id("com.google.dagger.hilt.android") version "2.44"

}

android {
    namespace = "com.bangkit.dermascan"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bangkit.dermascan"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String","BASE_URL","\"https://dermascan-api-dev-991773542009.asia-southeast2.run.app/\"")
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
        buildConfig = true
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


}

dependencies {
    // Core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt dependencies
    implementation(libs.hilt.android)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.runtime.livedata)
    kapt(libs.hilt.compiler) // Gunakan kapt untuk Hilt compiler

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
//    implementation("androidx.lifecycle:lifecycle-livedata-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
//    implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")
//    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
//    implementation ("com.google.firebase:firebase-auth-ktx:21.0.1")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("androidx.navigation:navigation-compose:2.5.3")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
//    implementation("androidx.activity:activity-ktx:1.7.2")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
//    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")

    implementation ("androidx.cardview:cardview:1.0.0")

    implementation ("androidx.camera:camera-core:1.4.0")
    implementation ("androidx.camera:camera-camera2:1.4.0")
    implementation ("androidx.camera:camera-lifecycle:1.4.0")
    implementation ("androidx.camera:camera-view:1.4.0")
    implementation ("androidx.exifinterface:exifinterface:1.3.6")

//    implementation("com.google.dagger:hilt-android:2.44")

}