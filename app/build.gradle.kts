plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.group5.miniproject2"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.group5.miniproject2"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    // --- Giữ nguyên từ file gốc ---
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    // --- Room Database ---
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // --- Navigation Component ---
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // --- Lifecycle: ViewModel + LiveData ---
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)

    // --- Glide (load ảnh) ---
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // --- Lottie (animation) ---
    implementation(libs.lottie)

    // --- CircleImageView ---
    implementation(libs.circleimageview)

    // --- SwipeRefreshLayout ---
    implementation(libs.swiperefreshlayout)

    // --- ViewPager2 ---
    implementation(libs.viewpager2)

}