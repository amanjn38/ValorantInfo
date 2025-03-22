package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.valorantinfo.MockKInitializer
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.Category
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    private lateinit var scenario: FragmentScenario<TestHomeFragment>

    @Before
    fun setup() {
        // Initialize MockK
        MockKInitializer.initialize()
    }
    
    private fun launchFragment() {
        // Launch the fragment with a simple theme
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_ValorantInfo_NoActionBar // Use app's NoActionBar theme
        ) {
            TestHomeFragment()
        }
    }

    @Test
    fun testHomeFragmentDisplaysCategories() {
        // Launch the fragment
        launchFragment()
        
        // This test just verifies that the fragment loads and displays without crashing
        // Add more assertions as needed
    }
} 