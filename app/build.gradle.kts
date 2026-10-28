plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.maternatrack.app"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.maternatrack.app"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0-uts"
    }

    buildTypes {
        release {
            // R8 memangkas dan mengoptimasi kode agar Compose berjalan mulus dan APK lebih kecil
            optimization {
                enable = true
            }
            // APK pengumpulan UTS ditandatangani dengan kunci debug agar bisa langsung dipasang
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    debugImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.junit)
}
