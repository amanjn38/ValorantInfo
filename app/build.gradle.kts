plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.valorantinfo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.valorantinfo"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.valorantinfo.HiltTestRunner"
        
        // Required for MockK to work properly with Android instrumented tests
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
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
    
    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
            excludes += "/META-INF/AL2.0"
            excludes += "/META-INF/LGPL2.1"
            // Exclude JNI files
            jniLibs.useLegacyPackaging = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.useJUnitPlatform()
            }
        }
        animationsDisabled = true
    }
}

// Define consistent versions for test dependencies
val testCoreVersion = "1.5.0"
val espressoVersion = "3.5.1"
val testExtVersion = "1.1.5"
val testRunnerVersion = "1.5.2"
val testRulesVersion = "1.5.0"
val mockkVersion = "1.13.8" // Use a more stable version

configurations.all {
    resolutionStrategy {
        // Force a specific version of androidx.test:core to resolve conflicts
        force("androidx.test:core:$testCoreVersion")
        force("androidx.test:core-ktx:$testCoreVersion")
        force("androidx.test.ext:junit:$testExtVersion")
        force("androidx.test.ext:junit-ktx:$testExtVersion")
        force("androidx.test:runner:$testRunnerVersion")
        force("androidx.test:rules:$testRulesVersion")
        force("androidx.test.espresso:espresso-core:$espressoVersion")
        force("androidx.test.espresso:espresso-contrib:$espressoVersion")
        force("androidx.test.espresso:espresso-intents:$espressoVersion")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.glide)
    kapt(libs.compiler)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.androidx.core.splashscreen)
    
    // Testing dependencies
    testImplementation(libs.mockk.v11310)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.runner.v152)
    androidTestImplementation(libs.androidx.rules.v150)
    androidTestImplementation(libs.arch.core.testing)
    androidTestImplementation(libs.dexmaker.mockito)
    debugImplementation("androidx.fragment:fragment-testing:1.8.6")
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)

    // UI Testing dependencies with consistent versions
    androidTestImplementation(libs.androidx.core.v150)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.runner.v152)
    androidTestImplementation(libs.androidx.rules.v150)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(libs.androidx.espresso.contrib.v351)
    androidTestImplementation(libs.androidx.espresso.intents.v351)
    
    // Hilt testing
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${libs.versions.hiltAndroid.get()}")
    
    // Fragment testing (use consistent version with test:core)
    val fragmentVersion = "1.6.2" // Use a version compatible with test:core:1.5.0
    debugImplementation("androidx.fragment:fragment-testing:$fragmentVersion")
    debugImplementation("androidx.fragment:fragment-testing:$fragmentVersion")
    
    // MockK for Android instrumented tests (with the correct configuration)
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion") {
        exclude(group = "io.mockk", module = "mockk-agent-jvm")
    }
    // Use this version for Android instrumentation tests to avoid need for jvmti agent
    androidTestImplementation("io.mockk:mockk-agent-api:$mockkVersion")
    androidTestImplementation("io.mockk:mockk-agent:$mockkVersion")
}