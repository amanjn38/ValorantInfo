package com.example.valorantinfo

import io.mockk.MockKAnnotations

/**
 * Helper class to initialize MockK for Android instrumented tests
 * This helps avoid the jvmti agent initialization issues
 */
object MockKInitializer {
    fun initialize() {
        try {
            // Initialize MockK for Android tests
            MockKAnnotations.init(this, relaxUnitFun = true)
        } catch (e: Exception) {
            // Log the exception but continue
            println("MockK initialization failed: ${e.message}")
            e.printStackTrace()
        }
    }
}
