package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.valorantinfo.MockKInitializer
import com.example.valorantinfo.R
import com.example.valorantinfo.data.models.agent.Agent
import com.example.valorantinfo.data.models.agent.AgentResponse
import com.example.valorantinfo.ui.viewmodels.AgentViewModel
import com.example.valorantinfo.utilities.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AgentsFragmentTest {
    
    private lateinit var viewModel: AgentViewModel
    private lateinit var scenario: FragmentScenario<TestAgentsFragment>
    private val agentsStateFlow = MutableStateFlow<Resource<AgentResponse>>(Resource.Loading())
    private val filteredAgentsFlow = MutableStateFlow<List<Agent>>(emptyList())

    @Before
    fun setup() {
        // Initialize MockK
        MockKInitializer.initialize()
        
        // Create a relaxed mock of the ViewModel
        viewModel = mockk(relaxed = true)
        
        // Set up the state flow behavior
        every { viewModel.agentsState } returns agentsStateFlow
        every { viewModel.filteredAgents } returns filteredAgentsFlow
    }
    
    private fun launchFragment() {
        // Launch the fragment with the mocked ViewModel
        scenario = launchFragmentInContainer(
            themeResId = R.style.Theme_ValorantInfo_NoActionBar
        ) {
            TestAgentsFragment().apply {
                this.viewModel = this@AgentsFragmentTest.viewModel
            }
        }
    }

    @Test
    fun testAgentsFragmentLoadingState() {
        // Set the loading state
        agentsStateFlow.value = Resource.Loading()
        
        // Launch fragment after setting state
        launchFragment()
        
        // Add assertions as needed
        // For now, this is just verifying that the fragment loads without crashing
    }

    @Test
    fun testAgentsFragmentSuccessState() {
        // Create a test list of agents
        val agents = listOf(
            Agent(
                uuid = "test-uuid",
                displayName = "Test Agent",
                description = "Test description",
                developerName = "Test Dev Name",
                releaseDate = "2020-01-01",
                characterTags = emptyList(),
                displayIcon = "test-icon-url",
                displayIconSmall = "test-icon-small-url",
                bustPortrait = null,
                fullPortrait = "test-portrait-url",
                killfeedPortrait = null,
                background = "test-background-url",
                backgroundGradientColors = null,
                assetPath = "",
                isFullPortraitRightFacing = false,
                isPlayableCharacter = true,
                isAvailableForTest = true,
                isBaseContent = true,
                role = null,
                abilities = emptyList(),
                voiceLine = null
            )
        )
        
        // Set the filtered agents and success state
        filteredAgentsFlow.value = agents
        agentsStateFlow.value = Resource.Success(
            AgentResponse(
                status = 200,
                data = agents
            )
        )
        
        // Launch fragment after setting state
        launchFragment()
        
        // Add assertions as needed
        // For now, this is just verifying that the fragment handles success state without crashing
    }
}