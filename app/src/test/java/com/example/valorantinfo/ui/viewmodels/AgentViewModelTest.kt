package com.example.valorantinfo.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.valorantinfo.data.models.agent.Agent
import com.example.valorantinfo.data.models.agent.AgentResponse
import com.example.valorantinfo.data.models.agent.Role
import com.example.valorantinfo.repository.AgentRepository
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AgentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AgentViewModel
    private lateinit var repository: AgentRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = AgentViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getAgents is called, loading state is emitted first`() = runTest {
        // Given
        val mockResponse = mockAgentResponse()
        coEvery { repository.fetchAgents() } returns flowOf(
            Resource.Loading(),
            Resource.Success(mockResponse),
        )

        // When
        viewModel.agentsState.test {
            // Assert initial state is Loading
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            // Trigger the agent fetch
            viewModel.getAgents()

            // Skip the initial Loading state
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
    fun `when getAgents succeeds, filteredAgents contains only playable characters`() = runTest {
        // Given
        val mockResponse = mockAgentResponse()
        coEvery { repository.fetchAgents() } returns flowOf(
            Resource.Success(mockResponse),
        )

        // When
        viewModel.filteredAgents.test {
            // Assert initial state is empty list
            assertThat(awaitItem()).isEmpty()

            // Trigger the agent fetch
            viewModel.getAgents()

            // Assert filtered agents
            val agents = awaitItem()
            assertThat(agents).hasSize(2) // Only playable characters
            assertThat(agents.map { it.displayName }).containsExactly("Jett", "Phoenix")

            // Cancel the collection
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when search query is set, filteredAgents are filtered correctly`() = runTest {
        // Given
        val mockResponse = mockAgentResponse()
        coEvery { repository.fetchAgents() } returns flowOf(
            Resource.Success(mockResponse),
        )

        // Load agents first
        viewModel.getAgents()
        testDispatcher.scheduler.advanceUntilIdle()

        // When & Then
        viewModel.filteredAgents.test {
            // Skip the initial emission (all agents)
            skipItems(1)

            // Set search query to "je"
            viewModel.setSearchQuery("je")

            // Assert filtered agents contain only Jett
            val filtered = awaitItem()
            assertThat(filtered).hasSize(1)
            assertThat(filtered[0].displayName).isEqualTo("Jett")

            // Cancel the collection
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `when getAgents fails, error state is emitted`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { repository.fetchAgents() } returns flowOf(
            Resource.Error(errorMessage),
        )

        // When
        viewModel.agentsState.test {
            // Skip the initial loading state
            skipItems(1)

            // Trigger the agent fetch
            viewModel.getAgents()

            // Assert error state
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(Resource.Error::class.java)
            assertThat((errorState as Resource.Error).message).isEqualTo(errorMessage)

            // Cancel the collection
            cancelAndConsumeRemainingEvents()
        }
    }

    private fun mockAgentResponse(): AgentResponse {
        return AgentResponse(
            status = 200,
            data = listOf(
                Agent(
                    uuid = "1",
                    displayName = "Jett",
                    description = "Jett description",
                    displayIcon = "jett_icon.png",
                    fullPortrait = "jett_portrait.png",
                    background = "jett_background.png",
                    role = Role(
                        uuid = "r1",
                        displayName = "Duelist",
                        description = "Duelist description",
                        displayIcon = "duelist_icon.png",
                        assetPath = "ShooterGame/Content/Characters/AggroBot/AggroBot_PrimaryAsset",
                    ),
                    abilities = emptyList(),
                    isPlayableCharacter = true,
                    developerName = "Jett",
                    releaseDate = "2020-04-07",
                    characterTags = listOf("Fast", "Mobility"),
                    displayIconSmall = "jett_icon_small.png",
                    bustPortrait = "jett_bust.png",
                    killfeedPortrait = "jett_killfeed.png",
                    backgroundGradientColors = listOf("#12345", "#67890"),
                    assetPath = "Agents/Jett",
                    isFullPortraitRightFacing = false,
                    isAvailableForTest = true,
                    isBaseContent = true,
                    voiceLine = null,
                ),
                Agent(
                    uuid = "2",
                    displayName = "Phoenix",
                    description = "Phoenix description",
                    displayIcon = "phoenix_icon.png",
                    fullPortrait = "phoenix_portrait.png",
                    background = "phoenix_background.png",
                    role = Role(
                        uuid = "r1",
                        displayName = "Duelist",
                        description = "Duelist description",
                        displayIcon = "duelist_icon.png",
                        assetPath = "ShooterGame/Content/Characters/AggroBot/AggroBot_PrimaryAsset",
                    ),
                    abilities = emptyList(),
                    isPlayableCharacter = true,
                    developerName = "Phoenix",
                    releaseDate = "2020-04-07",
                    characterTags = listOf("Fire", "Flash"),
                    displayIconSmall = "phoenix_icon_small.png",
                    bustPortrait = "phoenix_bust.png",
                    killfeedPortrait = "phoenix_killfeed.png",
                    backgroundGradientColors = listOf("#ABCDE", "#FGHIJ"),
                    assetPath = "Agents/Phoenix",
                    isFullPortraitRightFacing = false,
                    isAvailableForTest = true,
                    isBaseContent = true,
                    voiceLine = null,
                ),
                Agent(
                    uuid = "3",
                    displayName = "Training Bot",
                    description = "Training Bot description",
                    displayIcon = "bot_icon.png",
                    fullPortrait = "bot_portrait.png",
                    background = "bot_background.png",
                    role = null,
                    abilities = emptyList(),
                    isPlayableCharacter = false,
                    developerName = "TrainingBot",
                    releaseDate = null,
                    characterTags = null,
                    displayIconSmall = "bot_icon_small.png",
                    bustPortrait = null,
                    killfeedPortrait = null,
                    backgroundGradientColors = emptyList(),
                    assetPath = "Agents/TrainingBot",
                    isFullPortraitRightFacing = false,
                    isAvailableForTest = false,
                    isBaseContent = true,
                    voiceLine = null,
                ),
            ),
        )
    }
}
