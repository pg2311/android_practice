plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mp3player"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mp3player"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release"){
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    viewBinding{
        enable = true
    }
}


dependencies {
// TODO.  미디어 알림 스타일(MediaStyle)을 사용하기 위해 필요합니다
    implementation("androidx.media:media:1.7.0") // 최신 버전으로 사용하세요.
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
