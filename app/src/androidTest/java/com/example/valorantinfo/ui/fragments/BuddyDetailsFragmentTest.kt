package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.buddy.Buddy
import com.example.valorantinfo.data.models.buddy.BuddyLevel
import com.example.valorantinfo.ui.adapters.BuddyLevelsAdapter
import com.example.valorantinfo.ui.viewmodels.BuddyDetailsViewModel
import com.example.valorantinfo.utilities.Resource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BuddyDetailsFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var scenario: FragmentScenario<BuddyDetailsFragment>
    private lateinit var viewModel: BuddyDetailsViewModel
    private lateinit var navController: NavController

    // Test data
    private val sampleBuddyUuid = "buddy-uuid"
    private val sampleLevelUuid = "level-uuid"
    
    private val sampleLevel = BuddyLevel(
        uuid = sampleLevelUuid,
        charmLevel = 1,
        hideIfNotOwned = false,
        displayName = "Test Level",
        displayIcon = "https://example.com/level.png",
        assetPath = "path/to/level"
    )
    
    private val sampleBuddy = Buddy(
        uuid = sampleBuddyUuid,
        displayName = "Test Buddy",
        isHiddenIfNotOwned = false,
        themeUuid = null,
        displayIcon = "https://example.com/buddy.png",
        assetPath = "path/to/buddy",
        levels = listOf(sampleLevel)
    )

    @Before
    fun setup() {
        hiltRule.inject()
        
        viewModel = mockk(relaxed = true)
        every { viewModel.buddyDetails } returns MutableStateFlow(Resource.Success(sampleBuddy))
        every { viewModel.selectedLevel } returns MutableStateFlow(Resource.Success(null))
        
        navController = mockk(relaxed = true)
        
        val bundle = Bundle().apply {
            putString("buddyUuid", sampleBuddyUuid)
        }
        
        scenario = launchFragmentInContainer(
            fragmentArgs = bundle,
            themeResId = R.style.Theme_ValorantInfo
        ) {
            BuddyDetailsFragment().apply {
//                viewModel = this@BuddyDetailsFragmentTest.viewModel
            }
        }
        
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
    }

    @Test
    fun test_buddyDetailsAreDisplayedCorrectly() {
        // Verify that the ViewModel was called to fetch buddy details
        verify { viewModel.fetchBuddyDetails(sampleBuddyUuid) }
        
        // Check that the buddy name is displayed
        onView(withId(R.id.tvBuddyName)).check(matches(withText("Test Buddy")))
        
        // Check that the levels section is visible
        onView(withId(R.id.tvLevelsLabel)).check(matches(isDisplayed()))
        onView(withId(R.id.tvLevelsHint)).check(matches(isDisplayed()))
        onView(withId(R.id.rvBuddyLevels)).check(matches(isDisplayed()))
    }
    
    @Test
    fun test_levelClickShowsBottomSheet() {
        // Given a buddy with levels
        val fragment = spyk<BuddyDetailsFragment>()
        scenario.onFragment {
//            fragment.buddyLevelsAdapter.submitList(listOf(sampleLevel))
            fragment.bottomSheetBehavior = mockk(relaxed = true)
        }
        
        // When clicking on a level
//        onView(withId(R.id.rvBuddyLevels)).perform(
//            RecyclerViewActions.actionOnItemAtPosition<BuddyLevelsAdapter.BuddyLevelViewHolder>(
//                0, click()
//            )
//        )
        
        // Then the bottom sheet should be shown
        verify { fragment.bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
        verify { viewModel.fetchBuddyLevel(sampleLevelUuid) }
    }
    
    @Test
    fun test_closeButtonHidesBottomSheet() {
        // Given a visible bottom sheet
        scenario.onFragment { fragment ->
            fragment.bottomSheetBehavior = mockk(relaxed = true)
            fragment.bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        
        // When clicking the close button
        onView(withId(R.id.btnCloseBottomSheet)).perform(click())
        
        // Then the bottom sheet should be hidden
        verify { viewModel.clearSelectedLevel() }
    }
} 