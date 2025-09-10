
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-parcelize")
}

android {
    namespace = "com.example.firebnb"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.firebnb"
        minSdk = 24
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
    buildFeatures{
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    testImplementation ("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation ("org.jetbrains.kotlin:kotlin-test:1.9.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Si nécessaire pour JSON
    testImplementation ("com.google.code.gson:gson:2.10.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    implementation ("com.google.code.gson:gson:2.8.5")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.2")
    testImplementation(libs.junit)
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.google.android.gms:play-services-maps:18.0.2")

}