// File build tingkat proyek: hanya mendaftarkan plugin, konfigurasi ada di app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
