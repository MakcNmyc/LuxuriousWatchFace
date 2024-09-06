plugins {
    id("com.android.application")

    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id ("org.jetbrains.kotlin.kapt")
    id ("androidx.navigation.safeargs.kotlin")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.shishkin.luxuriouswatchface"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.shishkin.luxuriouswatchface"
        minSdk = 28
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("androidx.fragment:fragment-ktx:1.5.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
//    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.wear.watchface:watchface:1.1.0")
    implementation("androidx.wear.watchface:watchface-editor:1.1.0")
    implementation("androidx.wear:wear:1.2.0")

//    implementation("androidx.viewpager2:viewpager2:1.0.0")
//    implementation("androidx.paging:paging-runtime:2.1.2")
    implementation("androidx.paging:paging-runtime:3.1.1")

    implementation("androidx.navigation:navigation-fragment-ktx:2.4.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.2")

    implementation("androidx.recyclerview:recyclerview:1.1.0")

    //Dependency injection
    implementation("com.google.dagger:dagger:2.42")
    kapt("com.google.dagger:dagger-compiler:2.42")
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")

    implementation(kotlin("reflect"))


}