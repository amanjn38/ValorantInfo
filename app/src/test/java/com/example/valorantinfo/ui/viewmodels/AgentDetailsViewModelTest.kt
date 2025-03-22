package com.example.valorantinfo.ui.viewmodels

import app.cash.turbine.test
import com.example.valorantinfo.data.models.agentDetails.Ability
import com.example.valorantinfo.data.models.agentDetails.AgentDetails
import com.example.valorantinfo.data.models.agentDetails.AgentDetailsResponse
import com.example.valorantinfo.data.models.agentDetails.Role
import com.example.valorantinfo.repository.AgentDetailsRepository
import com.example.valorantinfo.utilities.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AgentDetailsViewModelTest {

    private lateinit var viewModel: AgentDetailsViewModel
    private lateinit var repository: AgentDetailsRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = AgentDetailsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getAgentDetails is called, loading state is emitted first`() = runTest {
        // Given
        val agentUuid = "1"
        val mockResponse = mockAgentDetailsResponse()
        coEvery { repository.fetchAgentDetails(agentUuid) } returns flowOf(
            Resource.Loading(),
            Resource.Success(mockResponse)
        )

        // When
        viewModel.agentDetailsState.test {
            // Assert initial state is Loading
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            
            // Trigger the agent details fetch
            viewModel.getAgentDetails(agentUuid)
            
            // Skip the initial Loading state (it's also emitted from the repository flow)
            skipItems(1)
            
            // Assert Success state with data
            val successState = awaitItem()
            assertThat(successState).isInstanceOf(Resource.Success::class.java)
            assertThat((successState as Resource.Success).data).isEqualTo(mockResponse)
            
            // Cancel the collection
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when getAgentDetails fails, error state is emitted`() = runTest {
        // Given
        val agentUuid = "1"
        val errorMessage = "Network error"
        coEvery { repository.fetchAgentDetails(agentUuid) } returns flowOf(
            Resource.Error(errorMessage)
        )

        // When
        viewModel.agentDetailsState.test {
            // Skip the initial loading state
            skipItems(1)
            
            // Trigger the agent details fetch
            viewModel.getAgentDetails(agentUuid)
            
            // Assert error state
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(Resource.Error::class.java)
            assertThat((errorState as Resource.Error).message).isEqualTo(errorMessage)
            
            // Cancel the collection
            cancelAndConsumeRemainingEvents()
        }
    }

    private fun mockAgentDetailsResponse(): AgentDetailsResponse {
        return AgentDetailsResponse(
            status = 200,
            data = AgentDetails(
                uuid = "1",
                displayName = "Jett",
                description = "Jett description",
                displayIcon = "jett_icon.png",
                fullPortrait = "jett_portrait.png",
                fullPortraitV2 = "jett_portrait_v2.png",
                background = "jett_background.png",
                role = Role(
                    uuid = "r1",
                    displayName = "Duelist",
                    description = "Duelist description",
                    displayIcon = "duelist_icon.png",
                    assetPath = "ShooterGame/Content/Characters/AggroBot/AggroBot_PrimaryAsset"
                ),
                abilities = listOf(
                    Ability(
                        slot = "Q",
                        displayName = "Updraft",
                        description = "Updraft description",
                        displayIcon = "updraft_icon.png"
                    ),
                    Ability(
                        slot = "E",
                        displayName = "Tailwind",
                        description = "Tailwind description",
                        displayIcon = "tailwind_icon.png"
                    )
                ),
                developerName = "Jett",
                characterTags = listOf("Fast", "Mobility"),
                displayIconSmall = "jett_icon_small.png",
                bustPortrait = "jett_bust_portrait.png",
                releaseDate = "2020-04-07",
                killfeedPortrait = "jett_killfeed.png",
                backgroundGradientColors = listOf("#12345", "#67890"),
                assetPath = "Agents/Jett",
                isFullPortraitRightFacing = false,
                isPlayableCharacter = true,
                isAvailableForTest = true,
                isBaseContent = true,
                voiceLine = null
            ),
            error = null
        )
    }
} 