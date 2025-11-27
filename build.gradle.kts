// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

// No specific / directly copied lines but this page was informed by https://developer.android.com/training/data-storage/room

dependencies {
    def room_version = "2.6.1"

    implementation "androidx.room:room-runtime:$room_version"
    annotationProcessor "androidx.room:room-compiler:$room_version"

    kapt "androidx.room:room-compiler:$room_version" //Kotlin
    implementation "androidx.room:room-ktx:$room_version" //Livedata
}

// END material from above link

