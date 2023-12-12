plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.capstoneproject.clothizeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capstoneproject.clothizeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // UI
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.activity:activity-ktx:1.8.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    // lottie gif
    implementation("com.airbnb.android:lottie:6.1.0")

    // View Model and Live Data
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // Database
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Data Store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Paging 3 + Mediator
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.room:room-paging:2.6.1")

    // Firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

}